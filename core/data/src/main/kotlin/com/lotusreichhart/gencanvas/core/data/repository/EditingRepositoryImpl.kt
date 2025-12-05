package com.lotusreichhart.gencanvas.core.data.repository

import android.net.Uri
import androidx.core.net.toUri
import com.lotusreichhart.gencanvas.core.domain.repository.EditingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.util.Stack
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditingRepositoryImpl @Inject constructor() : EditingRepository {
    companion object {
        private const val MAX_UNDO_STACK_SIZE = 6
    }

    private val _currentImageUri = MutableStateFlow<Uri?>(null)
    override val currentImageUri: StateFlow<Uri?> = _currentImageUri.asStateFlow()

    private val _canUndo = MutableStateFlow(false)
    override val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _canRedo = MutableStateFlow(false)
    override val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

    private val undoStack = Stack<String>()
    private val redoStack = Stack<String>()

    private val sessionTempFiles = mutableListOf<String>()

    override fun initializeSession(uri: Uri) {
        Timber.d("Initialize Session with: $uri")
        val current = _currentImageUri.value

        if (current == null || current != uri) {
            Timber.d("New Session Detected -> Clearing old data")
            clearSession()
            _currentImageUri.value = uri
        } else {
            Timber.d("Resume Session -> Keeping stack")
        }
        updateFlags()
    }

    override fun updateImage(newUri: Uri) {
        val currentUri = _currentImageUri.value

        if (currentUri != null) {
            if (undoStack.size >= MAX_UNDO_STACK_SIZE) {
                val oldestUriString = undoStack.removeAt(0)
                val oldestUri = oldestUriString.toUri()
                if (oldestUri.scheme == "file") {
                    oldestUri.path?.let { deleteFile(it) }
                }
            }
            undoStack.push(currentUri.toString())
        }

        cleanupRedoBranch()

        newUri.path?.let { sessionTempFiles.add(it) }

        _currentImageUri.value = newUri
        updateFlags()
    }

    override fun undo() {
        if (undoStack.isNotEmpty()) {
            val currentUri = _currentImageUri.value
            if (currentUri != null) {
                redoStack.push(currentUri.toString())
            }

            val prevUriString = undoStack.pop()
            val prevUri = prevUriString.toUri()

            _currentImageUri.value = prevUri
            updateFlags()
            Timber.d("Undo performed. Current: $prevUri")
        }
    }

    override fun redo() {
        if (redoStack.isNotEmpty()) {
            val currentUri = _currentImageUri.value
            if (currentUri != null) {
                undoStack.push(currentUri.toString())
            }

            val nextUriString = redoStack.pop()
            val nextUri = nextUriString.toUri()

            _currentImageUri.value = nextUri
            updateFlags()
            Timber.d("Redo performed. Current: $nextUri")
        }
    }

    override fun clearSession() {
        Timber.d("Clearing Session... Deleting ${sessionTempFiles.size} temp files.")

        val filesToDelete = ArrayList(sessionTempFiles)
        CoroutineScope(Dispatchers.IO).launch {
            filesToDelete.forEach { path ->
                deleteFile(path)
            }
        }
        sessionTempFiles.clear()

        undoStack.clear()
        redoStack.clear()
        _currentImageUri.value = null
        updateFlags()
    }

    private fun cleanupRedoBranch() {
        while (redoStack.isNotEmpty()) {
            val uriString = redoStack.pop()
            val uri = uriString.toUri()
            if (uri.scheme == "file") {
                uri.path?.let { deleteFile(it) }
            }
        }
    }

    private fun deleteFile(path: String) {
        try {
            val file = File(path)
            if (file.exists()) {
                if (file.delete()) {
                    Timber.v("Deleted file: $path")
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete file: $path")
        }
    }

    private fun updateFlags() {
        _canUndo.value = undoStack.isNotEmpty()
        _canRedo.value = redoStack.isNotEmpty()
    }
}
package com.lotusreichhart.gencanvas.core.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.lotusreichhart.gencanvas.core.domain.repository.EditingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
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
class EditingRepositoryImpl@Inject constructor(
    @param:ApplicationContext private val context: Context
) : EditingRepository {

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
        val currentPath = currentUri?.path

        if (currentPath != null) {
            if (undoStack.size >= MAX_UNDO_STACK_SIZE) {
                val oldestPath = undoStack.removeAt(0)
                deleteFile(oldestPath)
                Timber.d("Stack limit reached -> Deleted oldest undo step: $oldestPath")
            }
            undoStack.push(currentPath)
        }

        cleanupRedoBranch()

        newUri.path?.let { sessionTempFiles.add(it) }

        _currentImageUri.value = newUri
        updateFlags()
    }

    override fun undo() {
        if (undoStack.isNotEmpty()) {
            val currentPath = _currentImageUri.value?.path
            if (currentPath != null) {
                redoStack.push(currentPath)
            }

            val prevPath = undoStack.pop()
            _currentImageUri.value = File(prevPath).toUri()
            updateFlags()
            Timber.d("Undo performed. Current: $prevPath")
        }
    }

    override fun redo() {
        if (redoStack.isNotEmpty()) {
            val currentPath = _currentImageUri.value?.path
            if (currentPath != null) {
                undoStack.push(currentPath)
            }

            val nextPath = redoStack.pop()
            _currentImageUri.value = File(nextPath).toUri()
            updateFlags()
            Timber.d("Redo performed. Current: $nextPath")
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
        if (redoStack.isNotEmpty()) {
            Timber.d("Cleaning up ${redoStack.size} redo files (branch truncated)")
            while (redoStack.isNotEmpty()) {
                val path = redoStack.pop()
                deleteFile(path)
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
package com.lotusreichhart.gencanvas.feature.studio.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.lotusreichhart.gencanvas.feature.studio.domain.repository.EditingRepository
import dagger.hilt.android.qualifiers.ApplicationContext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import javax.inject.Inject

internal class EditingRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : EditingRepository {
    companion object {
        private const val MAX_UNDO_STACK_SIZE = 6
        private const val SESSION_DIR_PREFIX = "studio_session_"
    }

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _currentImageUri = MutableStateFlow<Uri?>(null)
    override val currentImageUri: StateFlow<Uri?> = _currentImageUri.asStateFlow()

    private val _canUndo = MutableStateFlow(false)
    override val canUndo: StateFlow<Boolean> = _canUndo.asStateFlow()

    private val _canRedo = MutableStateFlow(false)
    override val canRedo: StateFlow<Boolean> = _canRedo.asStateFlow()

    private val undoStack = ArrayDeque<String>()
    private val redoStack = ArrayDeque<String>()

    private var currentSessionDir: File? = null

    init {
        cleanupOldSessions()
    }

    override fun initializeSession(uri: Uri) {
        val current = _currentImageUri.value

        if (current == null || current != uri) {
            Timber.d("Starting new session for: $uri")

            clearSession()

            createSessionDir()

            _currentImageUri.value = uri
        }
        updateFlags()
    }

    override fun updateImage(newUri: Uri) {
        val currentUri = _currentImageUri.value

        if (currentUri != null) {
            if (undoStack.size >= MAX_UNDO_STACK_SIZE) {
                val oldestUriString = undoStack.removeFirst()
                deleteFileIfItIsTemp(oldestUriString)
            }
            undoStack.addLast(currentUri.toString())
        }

        cleanupRedoBranch()

        _currentImageUri.value = newUri
        updateFlags()
    }

    override fun undo() {
        if (undoStack.isNotEmpty()) {
            val currentUri = _currentImageUri.value
            if (currentUri != null) {
                redoStack.addLast(currentUri.toString())
            }

            val prevUriString = undoStack.removeLast()
            _currentImageUri.value = prevUriString.toUri()
            updateFlags()
        }
    }

    override fun redo() {
        if (redoStack.isNotEmpty()) {
            val currentUri = _currentImageUri.value
            if (currentUri != null) {
                undoStack.addLast(currentUri.toString())
            }

            val nextUriString = redoStack.removeLast()
            _currentImageUri.value = nextUriString.toUri()
            updateFlags()
        }
    }

    override fun clearSession() {
        undoStack.clear()
        redoStack.clear()
        _currentImageUri.value = null
        updateFlags()

        val dirToDelete = currentSessionDir
        if (dirToDelete != null) {
            repositoryScope.launch {
                deleteDirectory(dirToDelete)
            }
        }
        currentSessionDir = null
    }

    private fun cleanupRedoBranch() {
        while (redoStack.isNotEmpty()) {
            val uriString = redoStack.removeLast()
            Timber.d("cleanupRedoBranch: $uriString")
            deleteFileIfItIsTemp(uriString)
        }
    }

    private fun createSessionDir() {
        val dirName = "${SESSION_DIR_PREFIX}${System.currentTimeMillis()}"
        currentSessionDir = File(context.cacheDir, dirName).apply {
            mkdirs()
        }
    }

    private fun deleteFileIfItIsTemp(uriString: String) {
        repositoryScope.launch {
            try {
                val uri = uriString.toUri()

                when (uri.scheme) {
                    "file" -> {
                        val path = uri.path ?: return@launch
                        val file = File(path)

                        if (file.absolutePath.contains(context.cacheDir.absolutePath)) {
                            if (file.exists() && file.delete()) {
                                Timber.v("Deleted temp file (file scheme): $path")
                            }
                        }
                    }
                    "content" -> {
                        try {
                            val rowsDeleted = context.contentResolver.delete(uri, null, null)
                            if (rowsDeleted > 0) {
                                Timber.v("Deleted temp file (content scheme): $uri")
                            } else {
                                Timber.w("ContentResolver delete returned 0 for: $uri")
                            }
                        } catch (e: Exception) {
                            Timber.e(e, "Failed to delete content uri: $uri")
                        }
                    }
                    else -> {
                        Timber.d("Ignored deletion for scheme: ${uri.scheme} (safe)")
                    }
                }
            } catch (e: Exception) {
                Timber.e(e, "Error deleting file")
            }
        }
    }

    private fun deleteDirectory(dir: File) {
        try {
            if (dir.exists()) {
                dir.deleteRecursively()
                Timber.d("Deleted session directory: ${dir.name}")
            }
        } catch (e: Exception) {
            Timber.e(e, "Failed to delete directory")
        }
    }

    private fun cleanupOldSessions() {
        Timber.w("Cleaning up old sessions...")
        repositoryScope.launch {
            context.cacheDir.listFiles()?.forEach { file ->
                if (file.isDirectory &&
                    file.name.startsWith(SESSION_DIR_PREFIX) &&
                    file.absolutePath != currentSessionDir?.absolutePath
                ) {
                    deleteDirectory(file)
                }
            }
        }
    }

    private fun updateFlags() {
        _canUndo.value = undoStack.isNotEmpty()
        _canRedo.value = redoStack.isNotEmpty()
    }
}
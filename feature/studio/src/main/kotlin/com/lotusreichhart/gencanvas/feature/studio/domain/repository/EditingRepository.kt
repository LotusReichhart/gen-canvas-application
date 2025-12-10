package com.lotusreichhart.gencanvas.feature.studio.domain.repository

import android.net.Uri
import kotlinx.coroutines.flow.StateFlow

internal interface EditingRepository {
    val currentImageUri: StateFlow<Uri?>

    val canUndo: StateFlow<Boolean>
    val canRedo: StateFlow<Boolean>
    fun initializeSession(uri: Uri)
    fun updateImage(newUri: Uri)
    fun undo()
    fun redo()
    fun clearSession()
}
package com.lotusreichhart.gencanvas.feature.studio.presentation.components.filter

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.lotusreichhart.gencanvas.feature.studio.domain.model.StudioStyle
import com.lotusreichhart.gencanvas.feature.studio.domain.model.filter.FilterStyle
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import java.io.File

@SuppressLint("MissingPermission")
@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@Composable
internal fun FilterToolView(
    imageUri: Uri,
    activeStyle: StudioStyle?,
    shouldExecuteFilter: Boolean,
    onInteract: (Boolean) -> Unit,
    onFilterSuccess: (Uri) -> Unit,
    onFilterError: (Exception?) -> Unit
) {
    val context = LocalContext.current
    var mPhotoEditor: PhotoEditor? by remember { mutableStateOf(null) }

    LaunchedEffect(activeStyle) {
        if (activeStyle is FilterStyle) {
            mPhotoEditor?.setFilterEffect(activeStyle.filterEffect)

            if (activeStyle.isDefault) {
                onInteract(false)
            } else {
                onInteract(true)
            }
        }
    }

    LaunchedEffect(shouldExecuteFilter) {
        if (shouldExecuteFilter) {
            val editor = mPhotoEditor
            if (editor == null) {
                onFilterError(Exception("PhotoEditor is null"))
                return@LaunchedEffect
            }

            try {
                val file = File(
                    context.cacheDir,
                    "filter_result_${System.currentTimeMillis()}.jpg"
                )

                editor.saveAsFile(file.absolutePath, object : PhotoEditor.OnSaveListener {
                    override fun onSuccess(imagePath: String) {
                        val savedUri = Uri.fromFile(File(imagePath))
                        onFilterSuccess(savedUri)
                    }

                    override fun onFailure(exception: Exception) {
                        onFilterError(exception)
                    }
                })
            } catch (e: Exception) {
                onFilterError(e)
            }
        }
    }


    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val photoEditorView = PhotoEditorView(context)

            mPhotoEditor = PhotoEditor.Builder(context, photoEditorView)
                .setPinchTextScalable(true)
                .build()

            photoEditorView.source.setImageURI(imageUri)

            photoEditorView
        }
    )
}
package com.lotusreichhart.gencanvas.core.common.util

import android.content.Context
import android.net.Uri
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

fun Context.createTmpFileFromUri(uri: Uri, fileName: String = "temp_avatar.jpg"): File? {
    return try {
        val stream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, fileName)
        val outputStream = FileOutputStream(file)
        stream?.copyTo(outputStream)
        stream?.close()
        outputStream.close()
        file
    } catch (e: Exception) {
        Timber.e(e, "Lỗi tạo file tạm từ Uri")
        null
    }
}
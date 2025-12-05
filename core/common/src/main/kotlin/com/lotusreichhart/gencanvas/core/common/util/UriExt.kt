package com.lotusreichhart.gencanvas.core.common.util

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun Uri.toPhysicalFile(context: Context): File? {
    return try {
        if (this.scheme == "file") {
            return this.toFile()
        }

        val contentResolver = context.contentResolver
        val fileName = "temp_file_${System.currentTimeMillis()}.jpg"
        val tempFile = File(context.cacheDir, fileName)

        val inputStream: InputStream? = contentResolver.openInputStream(this)
        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        tempFile
    } catch (e: Exception) {
        Timber.e(e, "Lỗi khi chuyển đổi URI sang File: $this")
        null
    }
}
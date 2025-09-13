package com.example.noteultimate.create_and_edit_note

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object RealPathUtil {
    fun getRealPath(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = File.createTempFile("upload_", ".jpg", context.cacheDir)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
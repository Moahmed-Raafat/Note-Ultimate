package com.example.noteultimate.create_and_edit_note

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.noteultimate.common.AppConstants

object CloudinaryUploader {
    fun initCloudinary(context: Context) {

        try {
            MediaManager.get()
        } catch (e: Exception) {
            val config = hashMapOf(
                "cloud_name" to AppConstants.CLOUD_NAME,
                "api_key" to AppConstants.API_KEY,
                "api_secret" to AppConstants.API_SECRET
            )
            MediaManager.init(context.applicationContext, config)
        }
    }

    fun uploadImageToCloudinary(
        context: Context,
        uri: Uri,
        onUploaded: (String?) -> Unit
    ) {
        val filePath = RealPathUtil.getRealPath(context, uri)
        if (filePath == null) {
            Toast.makeText(context, "Failed to get image path", Toast.LENGTH_SHORT).show()
            onUploaded(null)
            return
        }

        MediaManager.get().upload(filePath)
            .option("resource_type", "image")
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    // Optional: show progress
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: Map<*, *>) {
                    val url = resultData["secure_url"] as? String
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show()
                        onUploaded(url)
                    }
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Upload Failed: ${error?.description}", Toast.LENGTH_SHORT).show()
                        onUploaded(null)
                    }
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            .dispatch()
    }
}
// Reuse functions easily
fun initCloudinary(context: Context) = CloudinaryUploader.initCloudinary(context)
fun uploadImageToCloudinary(
    context: Context,
    uri: Uri,
    onUploaded: (String?) -> Unit
) = CloudinaryUploader.uploadImageToCloudinary(context, uri, onUploaded)
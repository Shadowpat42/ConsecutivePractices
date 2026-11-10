package com.example.consecutivepractices.data.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileImageManager(private val context: Context) {
    suspend fun keepGalleryImage(uri: Uri): Uri = withContext(Dispatchers.IO) {
        try {
            context.contentResolver.takePersistableUriPermission(
                uri, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            uri
        } catch (e: SecurityException) {
            copyGalleryImage(uri)
        } catch (e: IllegalArgumentException) {
            copyGalleryImage(uri)
        }
    }

    suspend fun createCameraImage(): Uri = withContext(Dispatchers.IO) {
        val file = newImageFile("camera", ".jpg")
        fileUri(file)
    }

    private fun copyGalleryImage(uri: Uri): Uri {
        val file = newImageFile("gallery", ".img")
        try {
            val input = context.contentResolver.openInputStream(uri)
                ?: throw IOException("Image is unavailable")
            input.use { source -> file.outputStream().use { source.copyTo(it) } }
            return fileUri(file)
        } catch (e: Exception) {
            file.delete()
            throw e
        }
    }

    private fun newImageFile(prefix: String, suffix: String): File {
        val directory = File(context.filesDir, "profile")
        if (!directory.isDirectory && !directory.mkdirs()) {
            throw IOException("Cannot create profile directory")
        }
        return File.createTempFile(prefix, suffix, directory)
    }

    private fun fileUri(file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.profile.files", file)
}

package com.example.consecutivepractices.data.platform

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.webkit.URLUtil
import java.io.IOException
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

data class DownloadedResume(val uri: Uri, val mimeType: String)

class ResumeDownloader(private val context: Context) {
    private val manager = context.getSystemService(DownloadManager::class.java)

    suspend fun download(url: String): DownloadedResume = withContext(Dispatchers.IO) {
        val fileName = "${UUID.randomUUID()}-${URLUtil.guessFileName(url, null, null)}"
        val request = DownloadManager.Request(Uri.parse(url).normalizeScheme())
            .setTitle("Резюме")
            .setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)
        val id = manager.enqueue(request)
        var completed = false
        try {
            withTimeoutOrNull(120_000) { waitForDownload(id) }
                ?: throw IOException("Download timed out")
            val uri = manager.getUriForDownloadedFile(id) ?: throw IOException("File unavailable")
            val mimeType = manager.getMimeTypeForDownloadedFile(id)
                ?.takeUnless { it.isBlank() || it == "application/octet-stream" }
                ?: "application/pdf"
            completed = true
            DownloadedResume(uri, mimeType)
        } finally {
            if (!completed) manager.remove(id)
        }
    }

    private suspend fun waitForDownload(id: Long) {
        val query = DownloadManager.Query().setFilterById(id)
        while (true) {
            val status = manager.query(query)?.use { cursor ->
                if (!cursor.moveToFirst()) throw IOException("Download unavailable")
                cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            }
            when (status) {
                DownloadManager.STATUS_SUCCESSFUL -> return
                DownloadManager.STATUS_FAILED, null -> throw IOException("Download failed")
            }
            delay(400)
        }
    }
}

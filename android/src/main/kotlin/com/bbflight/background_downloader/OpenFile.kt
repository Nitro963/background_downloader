package com.bbflight.background_downloader

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import java.io.File


class OpenFileProvider : FileProvider(R.xml.bgd_file_paths)

/**
 * Opens the file at the given path or URI in the default file manager app.
 */
fun doOpenFile(
    activity: Activity,
    filePathOrUriString: String,
    mimeType: String,
    message: String = "Open with"
): Boolean {
    val uri = Uri.parse(filePathOrUriString)
    val intent = Intent(Intent.ACTION_VIEW)
    try {
        val contentUri =
            if (uri.scheme == "content" || uri.scheme == "file") uri
            else
                getUriForFile(
                    activity,
                    activity.packageName + ".com.bbflight.background_downloader.fileprovider",
                    File(filePathOrUriString)
                )
        
        intent.setDataAndType(contentUri, mimeType)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        )
        
        // Create the chooser intent with custom message
        val chooser = Intent.createChooser(intent, message)
        activity.startActivity(chooser)
        return true
    } catch (e: Exception) {
        Log.i(BDPlugin.TAG, "Failed to open file $filePathOrUriString: $e")
    }
    return false
}

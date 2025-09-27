package cn.randyma.exoplayerofflinedemo.download

import android.app.NotificationManager
import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import cn.randyma.exoplayerofflinedemo.tools.NotificationHelper
import java.lang.Exception

@UnstableApi
class DownloadManagerListener(private val context: Context): DownloadManager.Listener {

    override fun onDownloadChanged(
        downloadManager: DownloadManager,
        download: Download,
        finalException: Exception?
    ) {
        super.onDownloadChanged(downloadManager, download, finalException)

        NotificationHelper.showNotification(context, download)
    }



    override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
        super.onDownloadRemoved(downloadManager, download)

        val title = "Removed"
        val content = download.request.uri.path ?: ""

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)
            ?.notify(
                download.request.id.hashCode(),
                NotificationHelper.createNotificationBuilder(context, title, content).build()
            )
    }
}
package cn.randyma.exoplayerofflinedemo.download

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import cn.randyma.exoplayerofflinedemo.R
import cn.randyma.exoplayerofflinedemo.download.ExoPlayerDownloadService.Companion.NOTIFICATION_ID
import cn.randyma.exoplayerofflinedemo.tools.NotificationHelper
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import java.lang.Exception

class DownloadManagerListener(private val context: Context): DownloadManager.Listener {

    override fun onDownloadChanged(
        downloadManager: DownloadManager,
        download: Download,
        finalException: Exception?
    ) {
        super.onDownloadChanged(downloadManager, download, finalException)

        var notification: Notification? = null
        val content = download.request.uri.path ?: ""

        when (download.state) {
            Download.STATE_COMPLETED -> {
                val title = context.getString(R.string.download_completed)

                notification = NotificationHelper.createNotificationBuilder(context, title, content).build()
            }
            Download.STATE_DOWNLOADING -> {
                val title = context.getString(R.string.download_in_progress)

                notification = NotificationHelper.createNotificationBuilder(context, title, content, download.percentDownloaded.toInt()).build()
            }
            Download.STATE_FAILED -> {
                val title = context.getString(R.string.download_failed)
                notification = NotificationHelper.createNotificationBuilder(context, title, content).build()
            }
            Download.STATE_STOPPED -> {
                val title = context.getString(R.string.download_stopped)
                notification = NotificationHelper.createNotificationBuilder(context, title, content).build()
            }
            Download.STATE_REMOVING -> {
                val title = context.getString(R.string.download_removing)
                notification = NotificationHelper.createNotificationBuilder(context, title, content).build()
            }
        }

        val notificationId = NOTIFICATION_ID + 1 + download.request.id.hashCode()

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.let {
            if (notification == null) {
                it.cancel(notificationId)
            } else {
                it.notify(notificationId, notification)
            }
        }
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
package cn.randyma.exoplayerofflinedemo.tools

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.OptIn
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import cn.randyma.exoplayerofflinedemo.MainActivity
import cn.randyma.exoplayerofflinedemo.R
import cn.randyma.exoplayerofflinedemo.download.ExoPlayerDownloadService

private const val MAX = 100

object NotificationHelper {
    private const val channelId = "Download Demo"

    private fun createNotificationChannel(context: Context, channelId: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT)
            (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(notificationChannel)
        }

        return channelId
    }

    fun createNotificationBuilder(context: Context, title: String, content: String, progress: Int = -1): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context, createNotificationChannel(context, channelId))
            .setOngoing(true)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE))

        if (progress > -1) {
            builder.setProgress(MAX, progress, false)
        }

        return builder
    }

    @OptIn(UnstableApi::class)
    fun createNotification(context: Context, download: Download): Notification {
        var notification: Notification? = null
        val content = download.request.uri.path ?: ""

        when (download.state) {
            Download.STATE_COMPLETED -> {
                val title = context.getString(R.string.download_completed)

                notification =
                    createNotificationBuilder(context, title, content).build()
            }
            Download.STATE_DOWNLOADING -> {
                val title = context.getString(R.string.download_in_progress)

                notification = createNotificationBuilder(
                    context,
                    title,
                    content,
                    (download.percentDownloaded).toInt()
                )
                    .build()
            }
            Download.STATE_FAILED -> {
                val title = context.getString(R.string.download_failed)
                notification =
                    createNotificationBuilder(context, title, content).build()
            }
            Download.STATE_STOPPED -> {
                val title = context.getString(R.string.download_stopped)
                notification =
                    createNotificationBuilder(context, title, content).build()
            }
            Download.STATE_REMOVING -> {
                val title = context.getString(R.string.download_removing)
                notification =
                    createNotificationBuilder(context, title, content).build()
            }

            else -> {
                notification = createNotificationBuilder(context, context.getString(R.string.start_downloading), context.getString(R.string.download_in_progress))
                    .build()
            }
        }

        return notification!!
    }

    fun showNotification(context: Context, download: Download) {
        val notification = createNotification(context, download)

        val notificationId = ExoPlayerDownloadService.NOTIFICATION_ID + 1 + download.request.id.hashCode()

        (context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager)?.let {
            if (notification == null) {
                it.cancel(notificationId)
            } else {
                it.notify(notificationId, notification)
            }
        }
    }
}
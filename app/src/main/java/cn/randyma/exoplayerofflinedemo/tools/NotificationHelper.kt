package cn.randyma.exoplayerofflinedemo.tools

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import cn.randyma.exoplayerofflinedemo.MainActivity
import cn.randyma.exoplayerofflinedemo.R

object NotificationHelper {
    private const val channelId = "Download Demo"

    fun createNotificationChannel(context: Context, channelId: String): String {
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
            builder.setProgress(100, progress, false)
        }

        return builder
    }
}
package cn.randyma.exoplayerofflinedemo.download

import android.app.Notification
import android.content.Context
import android.widget.Toast
import cn.randyma.exoplayerofflinedemo.R
import cn.randyma.exoplayerofflinedemo.tools.NotificationHelper
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.scheduler.Scheduler
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExoPlayerDownloadService : DownloadService(
    NOTIFICATION_ID
), KoinComponent {

    private val exoDownloadManager: DownloadManager by inject()

    override fun getDownloadManager(): DownloadManager {
        return exoDownloadManager
    }

    override fun getScheduler(): Scheduler {
        return PlatformScheduler(this, JOB_ID)
    }

    override fun getForegroundNotification(
        downloads: MutableList<Download>,
        notMetRequirements: Int
    ): Notification {
        return if (downloads.isEmpty()) {
            NotificationHelper.createNotificationBuilder(
                this,
                this.getString(R.string.start_downloading),
                this.getString(R.string.download_in_progress)
            )
                .build()
        } else {
            NotificationHelper.createNotification(this, downloads[0])
        }
    }

    companion object {
        const val NOTIFICATION_ID = 1
        private const val JOB_ID = 1

        fun download(context: Context, downloadRequest: DownloadRequest) {
            sendAddDownload(context, ExoPlayerDownloadService::class.java, downloadRequest, true)
            Toast.makeText(
                context,
                String.format(
                    context.getString(R.string.start_downloading_item),
                    downloadRequest.uri.path
                ),
                Toast.LENGTH_SHORT
            ).show()
        }

        fun removeDownload(context: Context, downloadId: String) {
            sendRemoveDownload(context, ExoPlayerDownloadService::class.java, downloadId, true)
        }

        fun pauseDownloads(context: Context) {
            sendPauseDownloads(context, ExoPlayerDownloadService::class.java, true)
        }

        fun resumeDownloads(context: Context) {
            sendResumeDownloads(context, ExoPlayerDownloadService::class.java, true)
        }
    }

}
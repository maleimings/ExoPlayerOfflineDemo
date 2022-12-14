package cn.randyma.exoplayerofflinedemo.ui.download

import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager

class DownloadViewModel : ViewModel() {

    fun getDownloadedVideos(downloadManager: DownloadManager): List<Download> {
        val downloadCursor = downloadManager.downloadIndex.getDownloads(Download.STATE_COMPLETED)
        val downloadedList = mutableListOf<Download>()

        if (downloadCursor.moveToFirst()) {
            downloadedList.add(downloadCursor.download)

            while (downloadCursor.moveToNext()) {
                downloadedList.add(downloadCursor.download)
            }
        }

        return downloadedList
    }
}
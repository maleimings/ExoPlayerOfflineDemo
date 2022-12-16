package cn.randyma.exoplayerofflinedemo.ui.download

import androidx.lifecycle.ViewModel
import cn.randyma.exoplayerofflinedemo.tools.DownloadResourcesHelper
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager

class DownloadViewModel : ViewModel() {

    fun getDownloadedVideos(downloadManager: DownloadManager): List<Item> {
        val downloadCursor = downloadManager.downloadIndex.getDownloads(Download.STATE_COMPLETED)
        val downloadedList = mutableListOf<Download>()

        if (downloadCursor.moveToFirst()) {
            downloadedList.add(downloadCursor.download)

            while (downloadCursor.moveToNext()) {
                downloadedList.add(downloadCursor.download)
            }
        }

        val result = mutableListOf<Item>()
        for (download in downloadedList) {
            for (item in DownloadResourcesHelper.getVideoItems()) {
                if (item.url == download.request.uri.toString()) {
                    item.updateTimeMs = download.updateTimeMs
                    result.add(item)
                }
            }
        }

        return result
    }
}
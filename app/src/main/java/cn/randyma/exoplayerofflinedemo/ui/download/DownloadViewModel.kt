package cn.randyma.exoplayerofflinedemo.ui.download

import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import cn.randyma.exoplayerofflinedemo.tools.DownloadResourcesHelper

class DownloadViewModel : ViewModel() {

    @OptIn(UnstableApi::class)
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
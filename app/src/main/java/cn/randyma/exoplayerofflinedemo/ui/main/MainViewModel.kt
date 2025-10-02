package cn.randyma.exoplayerofflinedemo.ui.main

import android.content.Context
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager
import androidx.media3.exoplayer.drm.HttpMediaDrmCallback
import androidx.media3.exoplayer.offline.DownloadHelper
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import cn.randyma.exoplayerofflinedemo.tools.DownloadResourcesHelper
import cn.randyma.exoplayerofflinedemo.ui.download.Item

private const val AUTHENTICATION = "Authentication"

class MainViewModel : ViewModel() {

    fun getVideoUrls() = DownloadResourcesHelper.getVideoItems()

    @OptIn(UnstableApi::class)
    fun getDownloadHelper(context: Context, item: Item, dataSourceFactory: DataSource.Factory): DownloadHelper {

        val drmCallback = HttpMediaDrmCallback(item.drmLicenseUrl, dataSourceFactory)
        drmCallback.setKeyRequestProperty(AUTHENTICATION, item.token)

        val defaultDrmSessionManager = DefaultDrmSessionManager.Builder()
            .setKeyRequestParameters(HashMap<String, String>().apply {
                this[AUTHENTICATION] = item.token
            })
            .build(drmCallback)

        val mediaItem = MediaItem.Builder()
            .setUri(item.url)
            .setMimeType(item.mimetype)
            .build()

        return DownloadHelper.forMediaItem(mediaItem,
            DefaultTrackSelector.ParametersBuilder(context).build(),
            DefaultRenderersFactory(context),
            dataSourceFactory, defaultDrmSessionManager)
    }
}
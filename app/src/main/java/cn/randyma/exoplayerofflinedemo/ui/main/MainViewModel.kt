package cn.randyma.exoplayerofflinedemo.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import cn.randyma.exoplayerofflinedemo.tools.DownloadResourcesHelper
import cn.randyma.exoplayerofflinedemo.ui.download.Item
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource.Factory

private const val AUTHENTICATION = "Authentication"

class MainViewModel : ViewModel() {

    fun getVideoUrls() = DownloadResourcesHelper.getVideoItems()

    fun getDownloadHelper(context: Context, item: Item, dataSourceFactory: Factory): DownloadHelper {

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
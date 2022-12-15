package cn.randyma.exoplayerofflinedemo.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import cn.randyma.exoplayerofflinedemo.ui.download.Item
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource.Factory
import com.google.android.exoplayer2.util.MimeTypes

private const val AUTHENTICATION = "Authentication"

class MainViewModel : ViewModel() {
    private val dashVideoItems = mutableListOf<Item>()
    init {
        //see https://ottverse.com/free-mpeg-dash-mpd-manifest-example-test-urls/
        //Non-DRM contents
        dashVideoItems.add(
            Item(
                "https://livesim.dashif.org/livesim/chunkdur_1/ato_7/testpic4_8s/Manifest.mpd",
                mimetype = MimeTypes.APPLICATION_MPD
            )
        )

        dashVideoItems.add(
            Item(
                "https://dash.akamaized.net/dash264/TestCasesUHD/2b/11/MultiRate.mpd",
                mimetype = MimeTypes.APPLICATION_MPD
            )
        )

        dashVideoItems.add(
            Item(
                "https://dash.akamaized.net/dash264/TestCasesIOP33/adapatationSetSwitching/5/manifest.mpd",
                mimetype = MimeTypes.APPLICATION_MPD
            )
        )

        dashVideoItems.add(
            Item(
                "https://dash.akamaized.net/dash264/TestCases/2c/qualcomm/1/MultiResMPEG2.mpd",
                mimetype = MimeTypes.APPLICATION_MPD
            )
        )

        dashVideoItems.add(
            Item(
                "https://dash.akamaized.net/dash264/TestCasesHD/2b/qualcomm/1/MultiResMPEG2.mpd",
                mimetype = MimeTypes.APPLICATION_MPD
            )
        )

        dashVideoItems.add(
            Item(
                "https://dash.akamaized.net/dash264/TestCases/1b/qualcomm/1/MultiRatePatched.mpd",

                mimetype = MimeTypes.APPLICATION_MPD
            )
        )

        dashVideoItems.add(
            Item(
                "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd",

                mimetype = MimeTypes.APPLICATION_MPD
            )
        )

        dashVideoItems.add(
            Item(
                "http://media.developer.dolby.com/DolbyVision_Atmos/profile8.1_DASH/p8.1.mpd",

                mimetype = MimeTypes.APPLICATION_MPD
            )
        )

        //DRM contents
        dashVideoItems.add(
            Item(
                "https://storage.googleapis.com/wvmedia/cbc1/h264/tears/tears_aes_cbc1.mpd",
                drmLicenseUrl = "https://proxy.uat.widevine.com/proxy?provider=widevine_test",
                mimetype = MimeTypes.APPLICATION_MPD
            )
        )

        dashVideoItems.add(
            Item(
                "https://storage.googleapis.com/wvmedia/cbcs/h264/tears/tears_aes_cbcs.mpd",
                drmLicenseUrl = "https://proxy.uat.widevine.com/proxy?provider=widevine_test",
                mimetype = MimeTypes.APPLICATION_MPD
            )
        )
    }

    fun getVideoUrls() = dashVideoItems

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
package cn.randyma.exoplayerofflinedemo.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
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

    private val dashVideos = listOf(
        //see https://ottverse.com/free-mpeg-dash-mpd-manifest-example-test-urls/
        "https://livesim.dashif.org/livesim/chunkdur_1/ato_7/testpic4_8s/Manifest.mpd",
        "https://dash.akamaized.net/dash264/TestCasesUHD/2b/11/MultiRate.mpd",
        "https://dash.akamaized.net/dash264/TestCasesIOP33/adapatationSetSwitching/5/manifest.mpd",
        "https://dash.akamaized.net/dash264/TestCases/2c/qualcomm/1/MultiResMPEG2.mpd",
        "https://dash.akamaized.net/dash264/TestCasesHD/2b/qualcomm/1/MultiResMPEG2.mpd",
        "https://dash.akamaized.net/dash264/TestCases/1b/qualcomm/1/MultiRatePatched.mpd",
        "https://bitmovin-a.akamaihd.net/content/MI201109210084_1/mpds/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.mpd",
        "http://media.developer.dolby.com/DolbyVision_Atmos/profile8.1_DASH/p8.1.mpd",
        //see ExoPlayer Demo
        "https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd",
        "https://html5demos.com/assets/dizzy.mp4"
    )

    fun getVideoUrls() = dashVideos

    fun getDownloadHelper(context: Context, url: String, dataSourceFactory: Factory, drmLicenseUrl: String, token: String): DownloadHelper {

        val drmCallback = HttpMediaDrmCallback(drmLicenseUrl, dataSourceFactory)
        drmCallback.setKeyRequestProperty(AUTHENTICATION, token)

        val defaultDrmSessionManager = DefaultDrmSessionManager.Builder()
            .setKeyRequestParameters(HashMap<String, String>().apply {
                this[AUTHENTICATION] = token
            })
            .build(drmCallback)

        val mediaItem = MediaItem.Builder()
            .setUri(url)
            .setMimeType(MimeTypes.APPLICATION_MPD)
            .build()

        return DownloadHelper.forMediaItem(mediaItem,
            DefaultTrackSelector.ParametersBuilder(context).build(),
            DefaultRenderersFactory(context),
            dataSourceFactory, defaultDrmSessionManager)
    }
}
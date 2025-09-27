package cn.randyma.exoplayerofflinedemo.ui.player

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.DefaultRenderersFactory
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager
import androidx.media3.exoplayer.drm.DefaultDrmSessionManagerProvider
import androidx.media3.exoplayer.drm.FrameworkMediaDrm
import androidx.media3.exoplayer.drm.HttpMediaDrmCallback
import androidx.media3.exoplayer.offline.DownloadHelper
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.util.DebugTextViewHelper
import cn.randyma.exoplayerofflinedemo.R
import cn.randyma.exoplayerofflinedemo.databinding.ActivityPlayerBinding
import cn.randyma.exoplayerofflinedemo.ui.download.Item

import org.koin.android.ext.android.inject

@UnstableApi
class PlayerActivity: AppCompatActivity() {
    private val cache: Cache by inject()
    private val dataSourceFactory: DataSource.Factory by inject()
    private val httpDataSourceFactory: HttpDataSource.Factory by inject()
    private val downloadManager: DownloadManager by inject()

    private var drmLicenseUrl: String = ""

    private val player by lazy {
        ExoPlayer.Builder(this)
            .setRenderersFactory(
                DefaultRenderersFactory(this.applicationContext)
                .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER))
            .setMediaSourceFactory(createMediaSourceFactory())
            .build()
    }

    private val binding: ActivityPlayerBinding by lazy {
        ActivityPlayerBinding.inflate(LayoutInflater.from(this))
    }

    private lateinit var debugViewHelper: DebugTextViewHelper

    private val drmSessionManagerProvider by lazy {
        DefaultDrmSessionManagerProvider()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.playerView.requestFocus()
    }

    override fun onStart() {
        super.onStart()
        initialisePlayer()
        binding.playerView.onResume()
    }

    override fun onStop() {
        super.onStop()
        binding.playerView.onPause()
        releasePlayer()
    }

    private fun initialisePlayer() {
        val mediaItem = createMediaItemFromIntent(intent)
        player.playWhenReady = true
        binding.playerView.player = player

        debugViewHelper = DebugTextViewHelper(player, binding.debugTextView)
        debugViewHelper.start()

        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(httpDataSourceFactory)
            .setCacheWriteDataSinkFactory(null)

        val downloadRequest = downloadManager.downloadIndex.getDownload(mediaItem.playbackProperties?.uri.toString())?.request

        if (downloadRequest == null) {
            Toast.makeText(this, R.string.unable_to_get_the_downloaded_content, Toast.LENGTH_LONG).show()
            return
        }

        val drmSessionManager =
            if (drmLicenseUrl.isNotEmpty()) {
                DefaultDrmSessionManager.Builder()
                    .setUuidAndExoMediaDrmProvider(
                        C.WIDEVINE_UUID,
                        FrameworkMediaDrm.DEFAULT_PROVIDER
                    )
                    .build(HttpMediaDrmCallback(drmLicenseUrl, dataSourceFactory)).apply {
                        this.setMode(
                            DefaultDrmSessionManager.MODE_PLAYBACK,
                            downloadRequest.keySetId
                        )
                    }
            } else {
                null
            }

        val mediaSource = DownloadHelper.createMediaSource(downloadRequest, cacheDataSourceFactory, drmSessionManager)

        player.setMediaSource(mediaSource)
        player.prepare()
    }

    private fun releasePlayer() {
        player.release()
        binding.playerView.player = null
        debugViewHelper.stop()
    }

    private fun createMediaSourceFactory(): MediaSource.Factory {
        drmSessionManagerProvider.setDrmHttpDataSourceFactory(httpDataSourceFactory)

        return DefaultMediaSourceFactory(this)
            .setDataSourceFactory(dataSourceFactory)
            .setDrmSessionManagerProvider(drmSessionManagerProvider)
    }

    private fun createMediaItemFromIntent(intent: Intent): MediaItem {
        intent.getParcelableExtra<Item>(ITEM)?.let {
            drmLicenseUrl = it.drmLicenseUrl
            downloadManager.downloadIndex.getDownload(it.url)?.run {
                return MediaItem.fromUri(this.request.uri)
            }
        }

        return MediaItem.EMPTY
    }

    companion object {
        const val ITEM = "item"
    }
}
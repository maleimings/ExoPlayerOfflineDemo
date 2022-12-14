package cn.randyma.exoplayerofflinedemo.ui.player

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.randyma.exoplayerofflinedemo.R
import cn.randyma.exoplayerofflinedemo.databinding.ActivityPlayerBinding
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager
import com.google.android.exoplayer2.drm.DefaultDrmSessionManagerProvider
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.CacheDataSource
import com.google.android.exoplayer2.util.DebugTextViewHelper
import org.koin.android.ext.android.inject

class PlayerActivity: AppCompatActivity() {
    private val cache: Cache by inject()
    private val dataSourceFactory: DataSource.Factory by inject()
    private val httpDataSourceFactory: HttpDataSource.Factory by inject()
    private val downloadManager: DownloadManager by inject()

    private val player by lazy {
        ExoPlayer.Builder(this)
            .setRenderersFactory(DefaultRenderersFactory(this.applicationContext)
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

        val downloadMediaItem = downloadRequest.toMediaItem()

        val drmSessionManager = drmSessionManagerProvider.get(downloadMediaItem) as? DefaultDrmSessionManager
        drmSessionManager?.setMode(DefaultDrmSessionManager.MODE_PLAYBACK, downloadRequest.keySetId)

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
        intent.getStringExtra(URL)?.let {
            downloadManager.downloadIndex.getDownload(it)?.run {
                return MediaItem.fromUri(this.request.uri)
            }
        }

        return MediaItem.EMPTY
    }

    companion object {
        const val URL = "url"
    }
}
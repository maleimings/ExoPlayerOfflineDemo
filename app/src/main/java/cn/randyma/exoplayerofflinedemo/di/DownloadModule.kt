package cn.randyma.exoplayerofflinedemo.di

import cn.randyma.exoplayerofflinedemo.download.DownloadManagerListener
import com.google.android.exoplayer2.database.DatabaseProvider
import com.google.android.exoplayer2.database.StandaloneDatabaseProvider
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.upstream.cache.Cache
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.Executors

val downloadModule = module {

    single<DownloadManager> {
        val cache: Cache = get()
        val downloadManager = DownloadManager(get(), get(), cache, get(), Executors.newFixedThreadPool(4))
        downloadManager.addListener(DownloadManagerListener(get()))

        downloadManager
    }

    single<DatabaseProvider> { StandaloneDatabaseProvider(get()) }

    single<Cache> {
        val VIDEOS_DIR = "videos"
        val videosDir = File(androidContext().filesDir, VIDEOS_DIR)
        val databaseProvider: DatabaseProvider = get()
        SimpleCache(videosDir, NoOpCacheEvictor(), databaseProvider)
    }

    single<DataSource.Factory> {
        DefaultHttpDataSource.Factory()
    }

    single<HttpDataSource.Factory> {
        DefaultHttpDataSource.Factory()
    }
}
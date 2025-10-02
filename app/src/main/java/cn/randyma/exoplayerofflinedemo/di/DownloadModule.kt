package cn.randyma.exoplayerofflinedemo.di

import android.annotation.SuppressLint
import androidx.media3.database.DatabaseProvider
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.HttpDataSource
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.NoOpCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.offline.DownloadManager
import cn.randyma.exoplayerofflinedemo.download.DownloadManagerListener

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.Executors

@SuppressLint("UnsafeOptInUsageError")
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
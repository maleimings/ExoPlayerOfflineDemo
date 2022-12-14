package cn.randyma.exoplayerofflinedemo

import android.app.Application
import cn.randyma.exoplayerofflinedemo.di.downloadModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            modules(downloadModule)
        }
    }
}
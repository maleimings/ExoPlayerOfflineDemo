package cn.randyma.exoplayerofflinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import cn.randyma.exoplayerofflinedemo.databinding.ActivityMainBinding
import cn.randyma.exoplayerofflinedemo.ui.download.DownloadFragment
import cn.randyma.exoplayerofflinedemo.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainFragment by lazy {
        MainFragment.newInstance()
    }

    private val downloadFragment by lazy {
        DownloadFragment.newInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commitNow()
        }

        binding.fab.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, downloadFragment)
                .commitNow()
        }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.container)

        if (fragment is DownloadFragment) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, mainFragment)
                .commitNow()
        } else {
            super.onBackPressed()
        }
    }
}
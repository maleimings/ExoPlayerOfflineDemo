package cn.randyma.exoplayerofflinedemo.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cn.randyma.exoplayerofflinedemo.databinding.FragmentMainBinding
import cn.randyma.exoplayerofflinedemo.download.ExoPlayerDownloadService
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.upstream.HttpDataSource.Factory
import org.koin.android.ext.android.inject
import java.io.IOException

class MainFragment : Fragment() {

    private lateinit var videoList: List<String>
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private val dataSourceFactory: Factory by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        videoList = viewModel.getVideoUrls()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.run {
            adapter = UrlAdapter(videoList) { url ->
                viewModel.getDownloadHelper(requireContext(), url, dataSourceFactory, drmLicenseUrl = "", token = "")
                    .prepare(object : DownloadHelper.Callback {
                        override fun onPrepared(helper: DownloadHelper) {
                            ExoPlayerDownloadService.download(requireContext(), helper.getDownloadRequest(url, url.toByteArray()))
                        }

                        override fun onPrepareError(helper: DownloadHelper, e: IOException) {
                            Toast.makeText(requireContext(), "Download Failed to start ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    })
            }

            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}
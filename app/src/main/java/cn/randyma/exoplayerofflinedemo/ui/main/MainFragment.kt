package cn.randyma.exoplayerofflinedemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.randyma.exoplayerofflinedemo.databinding.FragmentMainBinding
import cn.randyma.exoplayerofflinedemo.download.ExoPlayerDownloadService
import cn.randyma.exoplayerofflinedemo.ui.download.Item
import com.google.android.exoplayer2.Format
import com.google.android.exoplayer2.drm.DrmSessionEventListener
import com.google.android.exoplayer2.drm.OfflineLicenseHelper
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.upstream.HttpDataSource.Factory
import org.koin.android.ext.android.inject
import java.io.IOException

class MainFragment : Fragment() {

    private lateinit var videoList: List<Item>
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
            adapter = ItemAdapter(videoList) { item ->
                viewModel.getDownloadHelper(requireContext(), item, dataSourceFactory)
                    .prepare(object : DownloadHelper.Callback {
                        override fun onPrepared(helper: DownloadHelper) {
                            var downloadRequest = helper.getDownloadRequest(item.url, item.url.toByteArray())
                            if (item.drmLicenseUrl.isNotEmpty()) {
                                val header = HashMap<String, String>()

                                if (item.token.isNotEmpty()) {
                                    header["AUTHENTICATION"] = item.token
                                }

                                val drmLicenseHelper = OfflineLicenseHelper.newWidevineInstance(item.drmLicenseUrl,
                                    true,
                                    dataSourceFactory,
                                    header,
                                    DrmSessionEventListener.EventDispatcher())

                                getFirstFormatWithDrmInitData(helper)?.let {
                                    val keySetId = drmLicenseHelper.downloadLicense(it)
                                    downloadRequest = downloadRequest.copyWithKeySetId(keySetId.copyOf())
                                    ExoPlayerDownloadService.download(requireContext(), downloadRequest)
                                    drmLicenseHelper.release()
                                }
                            } else {
                                ExoPlayerDownloadService.download(requireContext(), downloadRequest)
                            }
                        }

                        override fun onPrepareError(helper: DownloadHelper, e: IOException) {
                            Toast.makeText(requireContext(), "Download Failed to start ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    })
            }

            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    @Nullable
    private fun getFirstFormatWithDrmInitData(helper: DownloadHelper): Format? {
        for (periodIndex in 0 until helper.periodCount) {
            val mappedTrackInfo = helper.getMappedTrackInfo(periodIndex)
            for (rendererIndex in 0 until mappedTrackInfo.rendererCount) {
                val trackGroups = mappedTrackInfo.getTrackGroups(rendererIndex)
                for (trackGroupIndex in 0 until trackGroups.length) {
                    val trackGroup = trackGroups[trackGroupIndex]
                    for (formatIndex in 0 until trackGroup.length) {
                        val format: Format = trackGroup.getFormat(formatIndex)
                        if (format.drmInitData != null) {
                            return format
                        }
                    }
                }
            }
        }
        return null
    }
}
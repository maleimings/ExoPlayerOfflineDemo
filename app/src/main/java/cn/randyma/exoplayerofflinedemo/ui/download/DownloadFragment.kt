package cn.randyma.exoplayerofflinedemo.ui.download

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.randyma.exoplayerofflinedemo.databinding.FragmentDownloadBinding
import cn.randyma.exoplayerofflinedemo.ui.player.PlayerActivity
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import org.koin.android.ext.android.inject

class DownloadFragment : Fragment() {

    private lateinit var viewModel: DownloadViewModel
    private lateinit var downloadedVideos: List<Download>

    private var _binding: FragmentDownloadBinding? = null
    private val binding get() =  _binding!!

    private val downloadManager: DownloadManager by inject()

    companion object {
        fun newInstance() = DownloadFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[DownloadViewModel::class.java]

        downloadedVideos = viewModel.getDownloadedVideos(downloadManager)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.list.run {
            this.adapter = DownloadAdapter(downloadedVideos)
            {url ->
                val intent = Intent(requireContext(), PlayerActivity::class.java)
                intent.putExtra(PlayerActivity.URL, url)
                startActivity(intent)
            }
            this.layoutManager = LinearLayoutManager(requireContext())
        }
    }
}
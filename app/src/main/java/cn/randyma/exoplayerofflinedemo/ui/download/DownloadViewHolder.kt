package cn.randyma.exoplayerofflinedemo.ui.download

import androidx.recyclerview.widget.RecyclerView
import cn.randyma.exoplayerofflinedemo.databinding.DownloadItemBinding
import com.google.android.exoplayer2.offline.Download
import java.text.DateFormat
import java.util.Date

class DownloadViewHolder(private val binding: DownloadItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(download: Download, onClick: (String) -> Unit) {
        binding.url.text = download.request.uri.path ?: ""
        binding.details.text = DateFormat.getDateInstance().format(Date(download.updateTimeMs))
        binding.root.setOnClickListener {
            onClick.invoke(download.request.uri.toString())
        }
    }
}
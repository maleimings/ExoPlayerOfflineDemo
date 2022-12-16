package cn.randyma.exoplayerofflinedemo.ui.download

import androidx.recyclerview.widget.RecyclerView
import cn.randyma.exoplayerofflinedemo.R
import cn.randyma.exoplayerofflinedemo.databinding.DownloadItemBinding
import java.text.DateFormat
import java.util.Date

class DownloadViewHolder(private val binding: DownloadItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(download: Item, onClick: (Item) -> Unit) {
        binding.url.text = download.url ?: ""
        binding.details.text =
            binding.root.context.getString(
                R.string.downloaded_status,
                DateFormat.getDateInstance().format(Date(download.updateTimeMs)),
                if (download.drmLicenseUrl.isNotEmpty()) {
                    binding.root.context.getString(R.string.drm_content)
                } else {
                    ""
                }
            )
        binding.root.setOnClickListener {
            onClick.invoke(download)
        }
    }
}
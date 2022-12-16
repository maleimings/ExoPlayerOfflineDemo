package cn.randyma.exoplayerofflinedemo.ui.download

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.randyma.exoplayerofflinedemo.databinding.DownloadItemBinding

class DownloadAdapter(private val downloadList: List<Item>, private val onClick: (Item) -> Unit) :
    RecyclerView.Adapter<DownloadViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        return DownloadViewHolder(DownloadItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        holder.bind(downloadList[position], onClick)
    }

    override fun getItemCount(): Int {
        return downloadList.size
    }
}
package cn.randyma.exoplayerofflinedemo.ui.main

import androidx.recyclerview.widget.RecyclerView
import cn.randyma.exoplayerofflinedemo.databinding.ListItemBinding

class UrlViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(url: String, onClicked: (String) -> Unit) {
        binding.url.text = url
        binding.root.setOnClickListener {
            onClicked.invoke(url)
        }
    }
}
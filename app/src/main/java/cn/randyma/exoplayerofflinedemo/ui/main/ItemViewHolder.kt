package cn.randyma.exoplayerofflinedemo.ui.main

import androidx.recyclerview.widget.RecyclerView
import cn.randyma.exoplayerofflinedemo.databinding.ListItemBinding
import cn.randyma.exoplayerofflinedemo.ui.download.Item

class ItemViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Item, onClicked: (Item) -> Unit) {
        binding.url.text = item.url
        binding.root.setOnClickListener {
            onClicked.invoke(item)
        }
    }
}
package cn.randyma.exoplayerofflinedemo.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import cn.randyma.exoplayerofflinedemo.databinding.ListItemBinding
import cn.randyma.exoplayerofflinedemo.ui.download.Item

class ItemAdapter(private val list: List<Item>, private val onClick: (Item) -> Unit): Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position], onClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
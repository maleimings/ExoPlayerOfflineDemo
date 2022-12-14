package cn.randyma.exoplayerofflinedemo.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import cn.randyma.exoplayerofflinedemo.databinding.ListItemBinding

class UrlAdapter(private val list: List<String>, private val onClick: (String) -> Unit): Adapter<UrlViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrlViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context))
        return UrlViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UrlViewHolder, position: Int) {
        holder.bind(list[position], onClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
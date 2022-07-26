package com.adarsh.a7minuteworkoutapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adarsh.a7minuteworkoutapp.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val items : ArrayList<String>): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemHistoryRowBinding):RecyclerView.ViewHolder(binding.root){
        val llHistoryItemMain  = binding.llHistoryItemMain
        val tvItem = binding.tvItem
        val tvPositon = binding.tvPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHistoryRowBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date: String = items.get(position)
        holder.tvPositon.text = (position+1).toString()
        holder.tvItem.text= date
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
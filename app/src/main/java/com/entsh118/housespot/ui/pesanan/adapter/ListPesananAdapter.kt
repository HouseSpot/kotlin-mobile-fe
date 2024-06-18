package com.entsh118.housespot.ui.pesanan.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entsh118.housespot.data.api.response.DataItem
import com.entsh118.housespot.databinding.ItemPesananClientBinding

class ListPesananAdapter(private val listPesanan: List<DataItem?>): RecyclerView.Adapter<ListPesananAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e("CEK ORDER di adapter", listPesanan[0].toString())
        val binding =
            ItemPesananClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.namaVendor.text = listPesanan[position]?.idVendor
        holder.binding.type.text = listPesanan[position]?.serviceType
        holder.binding.start.text = listPesanan[position]?.startDate
        holder.binding.finish.text = listPesanan[position]?.endDate
        holder.binding.status.text = listPesanan[position]?.status
    }

    class ViewHolder(var binding: ItemPesananClientBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return listPesanan.size
    }
}
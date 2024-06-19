package com.entsh118.housespot.ui.layananjasa.adapter

import com.entsh118.housespot.data.api.response.VendorResponseItem
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.entsh118.housespot.databinding.ItemJasaBinding
import com.entsh118.housespot.ui.layananjasa.DetailJasaActivity

class ListVendorAdapter(private val listVendor: List<VendorResponseItem>): RecyclerView.Adapter<ListVendorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.e("CEK LIST di adapter",listVendor[0].toString())
        val binding = ItemJasaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.description.text = listVendor[position].deskripsiLayanan
       // holder.binding.harga = listVendor[position].
        holder.binding.rating.text = listVendor[position].rating.toString()
        holder.binding.titletxt.text = listVendor[position].pemilikInfo?.nama
        Glide.with(holder.itemView.context)
            .load(listVendor[position].portofolio?.get(0))
            .into(holder.binding.detailPhoto)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailJasaActivity::class.java)
            intent.putExtra(DetailJasaActivity.DETAIL_VENDOR, listVendor[position] )
            holder.itemView.context.startActivity(intent)
        }
    }

    class ViewHolder(var binding: ItemJasaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return listVendor.size
    }


}
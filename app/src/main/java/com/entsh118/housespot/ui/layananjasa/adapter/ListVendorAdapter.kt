package com.entsh118.housespot.ui.layananjasa.adapter

import com.entsh118.housespot.data.api.response.VendorResponseItem
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.entsh118.housespot.R
import com.entsh118.housespot.databinding.ItemJasaBinding
import com.entsh118.housespot.ui.layananjasa.DetailJasaActivity
class ListVendorAdapter(private val listVendor: List<VendorResponseItem>): RecyclerView.Adapter<ListVendorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (listVendor.isNotEmpty()) {
            Log.e("CEK LIST di adapter", listVendor[0].toString())
        } else {
            Log.e("CEK LIST di adapter", "List is empty")
        }
        val binding = ItemJasaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vendor = listVendor[position]
        holder.binding.description.text = vendor.deskripsiLayanan
        holder.binding.harga.text = "Start from Rp.${vendor.feeMinimum},-"
        holder.binding.rating.text = vendor.rating.toString()
        holder.binding.titletxt.text = vendor.pemilikInfo?.nama

        // Check if portofolio is not empty before loading image
        if (!vendor.portofolio.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(vendor.portofolio[0])
                .into(holder.binding.detailPhoto)
        } else {
            // Set a placeholder or clear the image if there is no portfolio
            holder.binding.detailPhoto.setImageResource(R.drawable.ic_place_holder)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailJasaActivity::class.java)
            intent.putExtra(DetailJasaActivity.DETAIL_VENDOR, vendor)
            holder.itemView.context.startActivity(intent)
        }
    }

    class ViewHolder(var binding: ItemJasaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return listVendor.size
    }
}

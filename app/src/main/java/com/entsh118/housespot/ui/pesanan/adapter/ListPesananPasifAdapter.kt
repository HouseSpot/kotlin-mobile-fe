package com.entsh118.housespot.ui.pesanan.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entsh118.housespot.data.api.response.DataItem
import com.entsh118.housespot.databinding.ItemPesananClientBinding
import com.entsh118.housespot.databinding.ItemPesananPasifBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ListPesananPasifAdapter(private val listPesanan: List<DataItem?>): RecyclerView.Adapter<ListPesananPasifAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPesananPasifBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPesanan[position]
        holder.binding.layanan.text = "${item?.serviceType} oleh Vendor"
        holder.binding.namaVendor.text = item?.idVendor
        holder.binding.budget.text = "Rp ${item?.budget} ,-"

        // Konversi startDate ke format tanggal bulan tahun
        val startDate = item?.startDate?.let { formatDate(it) }
        val endDate = item?.endDate?.let { formatDate(it) }

        holder.binding.waktu.text = "$startDate - $endDate"
        holder.binding.status.text = item?.status
    }

    override fun getItemCount(): Int {
        return listPesanan.size
    }

    private fun formatDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) return ""

        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date)
        } catch (e: Exception) {
            Log.e("ListPesananAdapter", "Error parsing date", e)
            ""
        }
    }

    class ViewHolder(val binding: ItemPesananPasifBinding) : RecyclerView.ViewHolder(binding.root)
}

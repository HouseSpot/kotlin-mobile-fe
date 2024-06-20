package com.entsh118.housespot.ui.pesanan.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.toUpperCase
import androidx.recyclerview.widget.RecyclerView
import com.entsh118.housespot.data.api.response.DataItem
import com.entsh118.housespot.databinding.ItemPesananClientBinding
import com.entsh118.housespot.ui.pesanan.FormFeedbackActivity
import java.text.SimpleDateFormat
import java.util.*

class ListPesananAdapter(private val listPesanan: List<DataItem?>): RecyclerView.Adapter<ListPesananAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPesananClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listPesanan[position]
        holder.binding.tvOrderTitle.text = "${item?.serviceType} oleh Vendor ${item?.idVendor}"
        holder.binding.tvOrderBudget.text = "Rp ${item?.budget} ,-"

        // Konversi startDate ke format tanggal bulan tahun
        val startDate = item?.startDate?.let { formatDate(it) }
        val endDate = item?.endDate?.let { formatDate(it) }

        holder.binding.tvOrderDate.text = "$startDate - $endDate"
        holder.binding.tvOrderStatus.text = item?.status

        holder.binding.kontak.visibility = if ( (item?.status=="ONGOING") || (item?.status=="WAITING"))
            View.VISIBLE else View.GONE
        holder.binding.buttonFeedback.visibility = if ( item?.status=="COMPLETED") View.VISIBLE else View.GONE

        if (item?.status=="COMPLETED") {
            holder.binding.buttonFeedback.setOnClickListener {
                val intent = Intent(holder.itemView.context, FormFeedbackActivity::class.java)
                intent.putExtra(FormFeedbackActivity.DETAIL_PESANAN, item)
                holder.itemView.context.startActivity(intent)
            }
        }

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

    class ViewHolder(val binding: ItemPesananClientBinding) : RecyclerView.ViewHolder(binding.root)
}

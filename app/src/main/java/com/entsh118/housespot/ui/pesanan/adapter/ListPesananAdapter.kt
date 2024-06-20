package com.entsh118.housespot.ui.pesanan.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.text.toUpperCase
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.entsh118.housespot.R
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
        holder.binding.tvOrderTitle.text = "${item?.serviceType} oleh ${item?.vendorInfo?.nama}"
        holder.binding.tvOrderBudget.text = "Rp ${item?.budget} ,-"

        // Konversi startDate ke format tanggal bulan tahun
        val startDate = item?.startDate?.let { formatDate(it) }
        val endDate = item?.endDate?.let { formatDate(it) }

        holder.binding.tvOrderDate.text = "$startDate - $endDate"
        holder.binding.tvOrderStatus.text = item?.status

        holder.binding.kontak.visibility = if  (item?.status=="ONGOING") View.VISIBLE else View.GONE
        holder.binding.buttonFeedback.visibility = if ( item?.status=="COMPLETED") View.VISIBLE else View.GONE

        if (item?.status=="COMPLETED") {
            holder.binding.buttonFeedback.setOnClickListener {
                val intent = Intent(holder.itemView.context, FormFeedbackActivity::class.java)
                intent.putExtra(FormFeedbackActivity.DETAIL_PESANAN, item)
                holder.itemView.context.startActivity(intent)
            }
        }
        val statusColor = when (item?.status) {
            "WAITING" -> R.color.waitingColor
            "ONGOING" -> R.color.ongoingColor
            "COMPLETED" -> R.color.completedColor
            "REJECTED" -> R.color.rejectedColor
            else -> R.color.black
        }
        holder.binding.tvOrderStatus.setBackgroundColor(ContextCompat.getColor(holder.binding.root.context, statusColor))

        // Set rounded background
        val roundedBackground = getRoundedBackgroundDrawable(holder.binding.root.context, statusColor, 8f)
        holder.binding.tvOrderStatus.background = roundedBackground


        holder.binding.kontak.setOnClickListener {
            val noHp = item?.vendorInfo?.noHp
            Log.d("VendorOrderDetailsActivity", "Phone number: $noHp")
            if (noHp != null) {
                val phoneNumber = if (noHp.startsWith("0")) {
                    "62" + noHp.substring(1)
                } else {
                    noHp
                }
                val url = "https://wa.me/$phoneNumber"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                holder.itemView.context.startActivity(intent)
            } else {
                Toast.makeText(holder.itemView.context, "Phone number is not available", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun getRoundedBackgroundDrawable(context: Context, colorResId: Int, cornerRadiusDp: Float): GradientDrawable {
        val color = ContextCompat.getColor(context, colorResId)
        val cornerRadiusPx = context.resources.displayMetrics.density * cornerRadiusDp
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(color)
            cornerRadius = cornerRadiusPx
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

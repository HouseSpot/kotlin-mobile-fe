package com.entsh118.housespot.ui.orderdetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entsh118.housespot.R
import com.entsh118.housespot.data.api.model.ReportLog

class ReportLogAdapter(private val reportLogs: List<ReportLog>) :
    RecyclerView.Adapter<ReportLogAdapter.ReportLogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportLogViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_report_log, parent, false)
        return ReportLogViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportLogViewHolder, position: Int) {
        val reportLog = reportLogs[position]
        holder.bind(reportLog)
    }

    override fun getItemCount(): Int = reportLogs.size

    class ReportLogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.tv_report_date)
        private val contentTextView: TextView = itemView.findViewById(R.id.tv_report_content)
        private val imageView: ImageView = itemView.findViewById(R.id.iv_report_image)

        fun bind(reportLog: ReportLog) {
            dateTextView.text = reportLog.date
            contentTextView.text = reportLog.content
            if (reportLog.imageUrl != null) {
                Glide.with(itemView.context)
                    .load(reportLog.imageUrl)
                    .into(imageView)
                imageView.visibility = View.VISIBLE
            } else {
                imageView.visibility = View.GONE
            }
        }
    }
}

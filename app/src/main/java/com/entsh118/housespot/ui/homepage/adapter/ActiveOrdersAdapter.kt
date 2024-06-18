package com.entsh118.housespot.ui.homepage.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.entsh118.housespot.R
import com.entsh118.housespot.data.api.model.Order
import com.entsh118.housespot.databinding.ItemOrderBinding

class ActiveOrdersAdapter(private val onItemClick: (Order) -> Unit) : ListAdapter<Order, ActiveOrdersAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
        holder.itemView.setOnClickListener { onItemClick(order) }
    }

    class OrderViewHolder(private val binding: ItemOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.tvOrderTitle.text = order.projectDescription
            binding.tvOrderBudget.text = "Rp. ${order.budget}"
            binding.tvOrderDate.text = "${order.startDate} - ${order.endDate}"
            binding.tvOrderStatus.text = order.status

            val statusColor = when (order.status) {
                "WAITING" -> R.color.waitingColor
                "ONGOING" -> R.color.ongoingColor
                "COMPLETED" -> R.color.completedColor
                "REJECTED" -> R.color.rejectedColor
                else -> R.color.black
            }
            binding.tvOrderStatus.setBackgroundColor(ContextCompat.getColor(binding.root.context, statusColor))

            // Set rounded background
            val roundedBackground = getRoundedBackgroundDrawable(binding.root.context, statusColor, 8f) // 8dp corner radius
            binding.tvOrderStatus.background = roundedBackground

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
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}

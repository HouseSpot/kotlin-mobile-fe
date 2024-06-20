package com.entsh118.housespot.ui.account.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entsh118.housespot.R

class PortofolioVendorAccountAdapter(private val portfolios: List<String?>) : RecyclerView.Adapter<PortofolioVendorAccountAdapter.PortfolioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PortfolioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_portfolio, parent, false)
        return PortfolioViewHolder(view)
    }

    override fun onBindViewHolder(holder: PortfolioViewHolder, position: Int) {
        val portfolio = portfolios[position]
        Glide.with(holder.itemView.context).load(portfolio).into(holder.ivPortfolio)
    }

    override fun getItemCount(): Int = portfolios.size

    inner class PortfolioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPortfolio: ImageView = itemView.findViewById(R.id.ivPortfolio)
    }
}

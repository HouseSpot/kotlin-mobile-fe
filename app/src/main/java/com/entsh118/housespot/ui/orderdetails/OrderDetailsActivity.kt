package com.entsh118.housespot.ui.orderdetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.entsh118.housespot.R
import com.entsh118.housespot.data.api.model.Order
import com.entsh118.housespot.databinding.ActivityOrderDetailsBinding
import com.entsh118.housespot.ui.orderdetails.adapter.OrderDetailsPagerAdapter
import com.entsh118.housespot.ui.orderdetails.viewmodel.OrderDetailsViewModel
import com.google.android.material.tabs.TabLayoutMediator

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding
    private val viewModel: OrderDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("OrderDetailsActivity", "tab_layout ID: ${binding.tabLayout.id}")
        Log.d("OrderDetailsActivity", "tab_progress_project ID: ${binding.tabLayout.findViewById<View>(
            R.id.tab_progress_project)?.id}")

        // Get order from intent or repository
        val order: Order = getOrderFromIntent()
        viewModel.setOrder(order)

        // Set up ViewPager with the fragments
        val adapter = OrderDetailsPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Progress Proyek"
                else -> "Informasi Proyek"
            }
        }.attach()

    }

    private fun getOrderFromIntent(): Order {
        // Fetch the order object passed from the previous activity
        return Order() // Replace with actual implementation
    }
}

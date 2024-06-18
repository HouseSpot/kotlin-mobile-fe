package com.entsh118.housespot.ui.homepage.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.databinding.FragmentOrderHistoryBinding
import com.entsh118.housespot.ui.homepage.adapter.OrderHistoryAdapter
import com.entsh118.housespot.ui.homepage.viewmodel.OrdersViewModel
import com.entsh118.housespot.ui.orderdetails.OrderDetailsActivity
import com.entsh118.housespot.ui.orderdetails.VendorOrderDetailsActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OrderHistoryFragment : Fragment() {

    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var viewModel: OrdersViewModel

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

        viewModel.orders.observe(viewLifecycleOwner, { orders ->
            if (orders.isNullOrEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
            } else {
                binding.tvEmptyState.visibility = View.GONE
                (binding.rvOrderHistory.adapter as OrderHistoryAdapter).submitList(orders)
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        dataStoreManager = DataStoreManager(requireContext())
        lifecycleScope.launch {
            val idVendor = dataStoreManager.userPreferencesFlow.first().id
            val statuses = "REJECTED,COMPLETED"

            viewModel.loadOrders(idVendor, statuses)
        }

        setupRecyclerView()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val idVendor = dataStoreManager.userPreferencesFlow.first().id
            val statuses = "REJECTED,COMPLETED"

            viewModel.loadOrders(idVendor, statuses)
        }
    }

    private fun setupRecyclerView() {
        binding.rvOrderHistory.layoutManager = LinearLayoutManager(requireContext())
        val adapter = OrderHistoryAdapter { order ->
            val intent = Intent(requireContext(), VendorOrderDetailsActivity::class.java)
            intent.putExtra("order_id", order.id)
            startActivity(intent)
        }
        binding.rvOrderHistory.adapter = adapter
    }
}

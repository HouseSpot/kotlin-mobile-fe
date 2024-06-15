package com.entsh118.housespot.ui.homepage.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entsh118.housespot.databinding.FragmentOrderHistoryBinding
import com.entsh118.housespot.ui.homepage.adapter.OrderHistoryAdapter
import com.entsh118.housespot.ui.homepage.viewmodel.OrdersViewModel

class OrderHistoryFragment : Fragment() {

    private lateinit var binding: FragmentOrderHistoryBinding
    private lateinit var viewModel: OrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

        setupRecyclerView()

        viewModel.orders.observe(viewLifecycleOwner, { orders ->
            (binding.rvOrderHistory.adapter as OrderHistoryAdapter).submitList(orders)
        })

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvOrderHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvOrderHistory.adapter = OrderHistoryAdapter()
    }
}

package com.entsh118.housespot.ui.homepage.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.entsh118.housespot.databinding.FragmentActiveOrdersBinding
import com.entsh118.housespot.ui.homepage.adapter.ActiveOrdersAdapter
import com.entsh118.housespot.ui.homepage.viewmodel.OrdersViewModel

class ActiveOrdersFragment : Fragment() {

    private lateinit var binding: FragmentActiveOrdersBinding
    private lateinit var viewModel: OrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentActiveOrdersBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(OrdersViewModel::class.java)

        setupRecyclerView()

        viewModel.orders.observe(viewLifecycleOwner, { orders ->
            (binding.rvActiveOrders.adapter as ActiveOrdersAdapter).submitList(orders)
        })

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.rvActiveOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rvActiveOrders.adapter = ActiveOrdersAdapter()
    }
}

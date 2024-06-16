package com.entsh118.housespot.ui.homepage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.entsh118.housespot.data.api.model.Order

class OrdersViewModel : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    init {
        loadOrders()
    }

    private fun loadOrders() {
        // Mock data
        val orders = listOf(
            Order(
                id = "1",
                id_pemesan = "user123",
                id_vendor = "vendor456",
                projectDescription = "Renovasi Rumah Bude Laras",
                serviceType = "Renovasi",
                propertyType = "Residential",
                materialProvider = "Kontraktor",
                startDate = "2024-07-01",
                endDate = "2024-12-01",
                budget = 500000000,
                status = "Waiting"
            )
        )
        _orders.value = orders
    }
}

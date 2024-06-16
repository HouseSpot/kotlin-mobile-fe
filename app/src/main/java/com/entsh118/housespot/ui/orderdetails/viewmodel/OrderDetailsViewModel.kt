package com.entsh118.housespot.ui.orderdetails.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.entsh118.housespot.data.api.model.Order


class OrderDetailsViewModel : ViewModel() {

    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order> get() = _order

    fun setOrder(order: Order) {
        _order.value = order
    }
}

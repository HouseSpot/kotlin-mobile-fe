package com.entsh118.housespot.ui.homepage.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entsh118.housespot.data.api.model.Order
import com.entsh118.housespot.data.api.response.OrderByVendorResponse
import com.entsh118.housespot.data.api.retrofit.ApiConfig.getOrderService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrdersViewModel : ViewModel() {

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> get() = _orders

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadOrders(idVendor: String, status: String? = null) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val service = getOrderService()
            val call = service.getOrdersByVendor(idVendor, status)
            call.enqueue(object : Callback<OrderByVendorResponse> {
                override fun onResponse(call: Call<OrderByVendorResponse>, response: Response<OrderByVendorResponse>) {
                    _isLoading.postValue(false)
                    if (response.isSuccessful) {
                        val orders = response.body()?.data?.filterNotNull() ?: emptyList()
                        Log.d("OrdersViewModel", "Orders received from API: $orders")
                        _orders.postValue(orders)
                    } else {
                        // Handle error case
                        Log.e("OrdersViewModel", "Error response: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<OrderByVendorResponse>, t: Throwable) {
                    _isLoading.postValue(false)
                    // Handle network error case
                    Log.e("OrdersViewModel", "API call failed: ${t.message}")
                }
            })
        }
    }
}

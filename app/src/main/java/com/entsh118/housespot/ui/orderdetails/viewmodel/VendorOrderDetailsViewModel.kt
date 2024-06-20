package com.entsh118.housespot.ui.orderdetails.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.entsh118.housespot.data.api.request.UpdateOrderRequest
import com.entsh118.housespot.data.api.response.Data
import com.entsh118.housespot.data.api.response.OrderDetailsResponse
import com.entsh118.housespot.data.api.response.ProfileResponse
import com.entsh118.housespot.data.api.response.UpdateOrderResponse
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VendorOrderDetailsViewModel(application: Application) : AndroidViewModel(application) {

    private val _orderDetails = MutableLiveData<Data?>()
    val orderDetails: LiveData<Data?> get() = _orderDetails

    private val _userDetails = MutableLiveData<ProfileResponse?>()
    val userDetails: LiveData<ProfileResponse?> get() = _userDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadOrderDetails(orderId: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val service = ApiConfig.getOrderService()
            service.getOrderDetailsById(orderId).enqueue(object : Callback<OrderDetailsResponse> {
                override fun onResponse(
                    call: Call<OrderDetailsResponse>,
                    response: Response<OrderDetailsResponse>
                ) {
                    _isLoading.postValue(false)
                    if (response.isSuccessful) {
                        val order = response.body()?.data
                        _orderDetails.postValue(order)
                        Log.d("VendorOrderDetailsViewModel", "Order details: $order")
                        order?.idPemesan?.let { userId ->
                            Log.d("VendorOrderDetailsViewModel", "User ID: $userId")
                            viewModelScope.launch(Dispatchers.Main) {
                                Log.d("VendorOrderDetailsViewModel", "Loading user details")
                                loadUserDetails(userId)
                            }
                        }
                    } else {
                        _orderDetails.postValue(null)  // Handle error case
                    }
                }

                override fun onFailure(call: Call<OrderDetailsResponse>, t: Throwable) {
                    _isLoading.postValue(false)
                    _orderDetails.postValue(null)
                }
            })
        }
    }

    private fun loadUserDetails(userId: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val service = ApiConfig.getAuthService()
                service.getUserProfile(userId).enqueue(object : Callback<ProfileResponse> {
                    override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                        if (response.isSuccessful) {
                            Log.d("VendorOrderDetailsViewModel", "User details response: ${response.body()}")
                            _userDetails.postValue(response.body())
                        } else {
                            _userDetails.postValue(null)  // Handle error case
                        }
                        _isLoading.postValue(false)
                        Log.d("VendorOrderDetailsViewModel", "User details: ${response.body()}")
                    }

                    override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                        _isLoading.postValue(false)
                        _userDetails.postValue(null)
                        Log.e("VendorOrderDetailsViewModel", "Error loading user details", t)
                    }
                })
            } catch (e: Exception) {
                _isLoading.postValue(false)
                _userDetails.postValue(null)
                Log.e("VendorOrderDetailsViewModel", "Error loading user details", e)
            }
        }
    }

    fun updateOrderStatus(orderId: String, status: String) {
        _isLoading.postValue(true)
        viewModelScope.launch {
            val service = ApiConfig.getOrderService()
            val updateOrderRequest = UpdateOrderRequest(status.uppercase())
            service.updateOrderStatus(orderId, updateOrderRequest).enqueue(object : Callback<UpdateOrderResponse> {
                override fun onResponse(
                    call: Call<UpdateOrderResponse>,
                    response: Response<UpdateOrderResponse>
                ) {
                    _isLoading.postValue(false)
                    if (response.isSuccessful) {
                        Toast.makeText(getApplication(), "Order status updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(getApplication(), "Failed to update order status", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateOrderResponse>, t: Throwable) {
                    _isLoading.postValue(false)
                    Toast.makeText(getApplication(), "Error updating order status", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

}

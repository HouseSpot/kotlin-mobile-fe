package com.entsh118.housespot.ui.account.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.data.api.response.VendorDetailsResponse
import com.entsh118.housespot.data.api.retrofit.VendorApiService
import com.entsh118.housespot.data.api.model.UserPreferences
import com.entsh118.housespot.data.api.response.Data
import com.entsh118.housespot.data.api.response.OrderDetailsResponse
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountProfilePageViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStoreManager = DataStoreManager(application)

    val userPreferencesFlow: Flow<UserPreferences?>
        get() = dataStoreManager.userPreferencesFlow

    private val _vendorDetails = MutableLiveData<VendorDetailsResponse?>()
    val vendorDetails: LiveData<VendorDetailsResponse?> get() = _vendorDetails

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun loadVendorDetails(vendorId: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val service = ApiConfig.getVendorService()
            service.getVendorById(vendorId).enqueue(object : Callback<VendorDetailsResponse> {
                override fun onResponse(
                    call: Call<VendorDetailsResponse>,
                    response: Response<VendorDetailsResponse>
                ) {
                    _isLoading.postValue(false)
                    if (response.isSuccessful) {
                        val vendor = response.body()
                        _vendorDetails.postValue(vendor)
                        Log.d("AccountProfilePageViewModel", "Vendor details: $vendor")
                    } else {
                        _vendorDetails.postValue(null)
                        Log.d("AccountProfilePageViewModel", "Failed to load vendor details; [NULL] response code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<VendorDetailsResponse>, t: Throwable) {
                    _isLoading.postValue(false)
                    _vendorDetails.postValue(null)
                    Log.e("AccountProfilePageViewModel", "Failed to load vendor details", t)
                }
            })
        }
    }
}

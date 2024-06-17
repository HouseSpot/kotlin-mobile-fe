package com.entsh118.housespot.ui.layananjasa.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.MutableLiveData
import com.entsh118.housespot.data.api.model.Order
import com.entsh118.housespot.data.api.model.PemilikInfo
import com.entsh118.housespot.data.api.model.Vendor
import com.entsh118.housespot.data.api.response.ErrorResponse
import com.entsh118.housespot.data.api.response.ResultState
import com.entsh118.housespot.data.api.response.VendorResponse
import com.entsh118.housespot.data.api.response.VendorResponseItem
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class ListVendorViewModel: ViewModel() {

    private val _listVendor = MutableLiveData<List<VendorResponseItem>>()
    val listVendor: LiveData<List<VendorResponseItem>> = _listVendor

    private val _vendors = MutableLiveData<List<Vendor>>()
    val vendors: LiveData<List<Vendor>> get() = _vendors

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    init {
        getVendor()
       // loadVendors()
    }
    fun getVendor() {
        val apiService = ApiConfig.getApiService()
        Log.e("FITUR LIST VENDOR","START FETCH")
        val client = ApiConfig.getApiService().getAllVendor()
        Log.e("FITUR LIST VENDOR","ON FETCH")
        Log.e("CEK API", client.toString())
        val response = apiService.getAllVendor()

        client.enqueue(object : Callback<VendorResponse> {
            override fun onResponse(
                call: Call<VendorResponse>,
                response: Response<VendorResponse>
            ) {
                Log.e("CEK API", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    // Log.d(LoginViewModel.TAG, "onResponse: ${response.body()}")
                    _listVendor.value = response.body()?.vendorResponse!!
                    Log.e("LIST VENDOR", listVendor.toString())
                }
            }
            override fun onFailure(call: Call<VendorResponse>, t: Throwable) {
                Log.e("FAILED", "onFailure: ${t.message}")
            }
        }
        )
        Log.e("CEK API", "AFTER RESPONSE")
    }



    private fun loadVendors() {
        // Mock data
        val vendors = listOf(
            Vendor(
                tipeLayanan = listOf("Renovasi", "Bangun dari awal"),
                profile = "Profile",
                rating = 3.93333333333333,
                deskripsiLayanan = "Haloo",
                iklanPersetujuan = "true",
                pemilikInfo = PemilikInfo(
                    email = "putri1234@gmail.com",
                    nama = "putri",
                    noHp = "08123456798"
                ),
                portofolio = listOf(
                    "http://localhost:3000/uploads/6d91965d67f4c9d0c2be9518e3fa5303e8104d2be49d32fd807583e8da7e89a4.png",
                    "http://localhost:3000/uploads/2cd294bf2a6d180fe73ec6b04359264a443af2d421ee0df4f39e221151217cfe.png",
                    "http://localhost:3000/uploads/bd5974857abdc53be4816b72e7a43e4aab31ac33aadabda45d56ac93a38b4f85.png"
                ),
                id = "1c9e5a82-0920-49aa-aa66-966eb0b56028",
                jenisProperti = "Residential",
                lokasiKantor = "Jakarta",
                jasaKontraktor = listOf("Kontraktor")
            )
        )
        _vendors.value = vendors
    }

}
package com.entsh118.housespot.ui.layananjasa.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.entsh118.housespot.data.api.model.PemilikInfo
import com.entsh118.housespot.data.api.model.Vendor
import com.entsh118.housespot.data.api.response.VendorResponseItem
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
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
       // val apiService = ApiConfig.
      //  Log.e("FITUR LIST VENDOR","START FETCH")
        val client = ApiConfig.getClientService().getAllVendor()
        Log.e("FITUR LIST VENDOR","ON FETCH")
        Log.e("CEK API", client.toString())
    //    val response = apiService.getAllVendor()

        client.enqueue(object : Callback<List<VendorResponseItem>> {
            override fun onResponse(
                call: Call<List<VendorResponseItem>>,
                response: Response<List<VendorResponseItem>>
            ) {
                if (response.isSuccessful) {
                    _listVendor.value = response.body()
                    Log.e("LIST VENDOR", listVendor.toString())
                } else {
                    _error.value = response.message()
                    Log.e("FITUR LIST VENDOR","ERROR")
                }
            }

            override fun onFailure(call: Call<List<VendorResponseItem>>, t: Throwable) {
                _error.value = t.message
                Log.e("FITUR LIST VENDOR","FAILURE")
            }

    }

    )
    }


    fun filterVendors(
        tipeLayanan: String?,
        jenisJasa: String?,
        hargaMinimum: Long?,
        hargaMaksimum: Long?
    ) {
        val client = ApiConfig.getClientService().filterVendors(
            tipeLayanan = tipeLayanan,
            jenisJasa = jenisJasa
        )

        Log.d("FILTER", "tipeLayanan: $tipeLayanan, jenisJasa: $jenisJasa, hargaMin: $hargaMinimum, hargaMax: $hargaMaksimum")


        client.enqueue(object : Callback<List<VendorResponseItem>> {
            override fun onResponse(
                call: Call<List<VendorResponseItem>>,
                response: Response<List<VendorResponseItem>>
            ) {
                if (response.isSuccessful) {
                    val vendors = response.body()
                    vendors?.let {
                        val filteredVendors = it.filter { vendor ->
                            val feeMinimum = vendor.feeMinimum?.toLong()
                            when {
                                hargaMinimum == null && hargaMaksimum == null -> true
                                hargaMinimum == null && hargaMaksimum != null -> feeMinimum!! <= hargaMaksimum
                                hargaMaksimum == null && hargaMinimum != null -> feeMinimum!! >= hargaMinimum
                                else -> feeMinimum in hargaMinimum!!..hargaMaksimum!!
                            }
                        }

                        _listVendor.value = filteredVendors
                    }
                } else {
                    _error.value = response.message()
                }
            }

            override fun onFailure(call: Call<List<VendorResponseItem>>, t: Throwable) {
                _error.value = t.message
            }
        })
    }
}
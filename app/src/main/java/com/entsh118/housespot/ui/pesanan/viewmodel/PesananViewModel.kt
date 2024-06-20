package com.entsh118.housespot.ui.pesanan.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.entsh118.housespot.data.api.model.Vendor
import com.entsh118.housespot.data.api.response.DataItem
import com.entsh118.housespot.data.api.response.OrderClientResponse
import com.entsh118.housespot.data.api.response.VendorResponseItem
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesananViewModel: ViewModel() {
    private val _listPesananAktif = MutableLiveData<List<DataItem?>?>()
    val listPesananAktif: MutableLiveData<List<DataItem?>?> = _listPesananAktif

    private val _listPesananPasif = MutableLiveData<List<DataItem?>?>()
    val listPesananPasif: MutableLiveData<List<DataItem?>?> = _listPesananPasif

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    fun getPesananAktif(id: String) {
        Log.e("FITUR LIST ORDER6","START LIST ORDER")
        val client = ApiConfig.getClientService().getAllOrder(id)
        Log.e("FITUR LIST ORDE7","START LIST ORDER")
        client.enqueue(object : Callback<OrderClientResponse> {
            override fun onResponse(
                call: Call<OrderClientResponse>,
                response: Response<OrderClientResponse>
            ) {
                Log.e("RESPONSE", response.isSuccessful.toString())
                if (response.isSuccessful) {
                    Log.e("FITUR LIST ORDER8","START LIST ORDER")
                    val activeOrders = mutableListOf<DataItem?>()
                    val passiveOrders = mutableListOf<DataItem?>()

                    response.body()?.data?.forEach { item ->
                        if (item?.status == "REJECTED" || item?.status == "COMPLETED") {
                            passiveOrders.add(item)
                        } else {
                            activeOrders.add(item)
                        }
                    }
                    _listPesananAktif.value = activeOrders
                    _listPesananPasif.value = passiveOrders
                    Log.e("LIST ORDER9", _listPesananAktif.toString().take(1))
                } else {
                    _error.value = response.message()
                    Log.e("ERROR",response.message().toString())
                }
            }

            override fun onFailure(call: Call<OrderClientResponse>, t: Throwable) {

            }

        }

        )
    }

}
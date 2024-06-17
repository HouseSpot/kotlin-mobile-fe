package com.entsh118.housespot.data.api.retrofit

import com.entsh118.housespot.data.api.response.VendorResponseItem
import retrofit2.Call
import retrofit2.http.*

interface ClientApiService {

    @GET("vendor/all")
    fun getAllVendor(
    ): Call<List<VendorResponseItem>>

}
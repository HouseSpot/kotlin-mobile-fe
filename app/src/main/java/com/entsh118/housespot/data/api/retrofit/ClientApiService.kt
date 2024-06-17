package com.entsh118.housespot.data.api.retrofit

import com.entsh118.housespot.data.api.response.RegisterResponse
import com.entsh118.housespot.data.api.response.VendorResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ClientApiService {

    @GET("vendor/all")
    fun getAllVendor(
    ): Call<VendorResponse>

}
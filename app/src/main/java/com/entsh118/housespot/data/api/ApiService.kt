package com.entsh118.housespot.data.api

import com.entsh118.housespot.data.api.model.VendorResponse
import com.entsh118.housespot.data.api.model.VendorResponseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("vendor/all")
    fun getAllVendor(
        //   @Header("Authorization") token: String,
        //  @Query("page") page: Int,
        //   @Query("size") size: Int,
    ): Call<VendorResponse>

    @GET("stories/{id}")
    fun getDetailVendor(
        //  @Header ("Authorization") token: String,
        @Path("id") id: String
    ): Call<VendorResponseItem>
}
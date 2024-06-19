package com.entsh118.housespot.data.api.retrofit

import com.entsh118.housespot.data.api.model.Order
import com.entsh118.housespot.data.api.response.DataItem
import com.entsh118.housespot.data.api.response.FileUploadResponse
import com.entsh118.housespot.data.api.response.OrderClientResponse
import com.entsh118.housespot.data.api.response.RegisterResponse
import com.entsh118.housespot.data.api.response.VendorResponseItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ClientApiService {

    @GET("vendor/all")
    fun getAllVendor(
    ): Call<List<VendorResponseItem>>

    @GET("orders/view_by_client/{id}")
    fun getAllOrder(
        @Path("id") id: String
    ): Call<OrderClientResponse>


    @FormUrlEncoded
    @POST("orders/add")
    fun addOrder(
        @Field("id_pemesan") idPemesan: String,
        @Field("id_vendor") idVendor: String,
        @Field("serviceType") serviceType: String,
        @Field("propertyType") propertyType: String,
        @Field("budget") budget: String,
        @Field("startDate") startDate: String,
        @Field("endDate") endDate: String,
        @Field("projectDescription") projectDescription: String,
        @Field("materialProvider") materialProvider: String
    ): Call<Void>

    @Multipart
    @POST("rating/add")
    suspend fun uploadFeedback(
        @Part("id_client") idClient: String,
        @Part("id_vendor") idVendor: String,
        @Part image: MultipartBody.Part,
        @Part("message") message: RequestBody,
        @Part("rating") rating: RequestBody,
    ): Call<FileUploadResponse>


}
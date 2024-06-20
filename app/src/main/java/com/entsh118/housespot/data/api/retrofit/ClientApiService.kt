package com.entsh118.housespot.data.api.retrofit

import com.entsh118.housespot.data.api.model.Order
import com.entsh118.housespot.data.api.response.DataItem
import com.entsh118.housespot.data.api.response.FileUploadResponse
import com.entsh118.housespot.data.api.response.OrderClientResponse
import com.entsh118.housespot.data.api.response.RatingResponse
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

    @GET("vendor/filter")
    fun filterVendors(
        @Query("harga_minimum") hargaMinimum: Long?, // Harga minimum
        @Query("harga_maksimum") hargaMaksimum: Long?, // Harga maksimum
        @Query("tipe_layanan") tipeLayanan: String?, // Jenis layanan (dalam bentuk string)
        @Query("jenis_jasa") jenisJasa: String?, // Jenis jasa (dalam bentuk string)
        @Query("nama_vendor") namaVendor: String? // Nama vendor untuk pencarian
    ): Call<List<VendorResponseItem>>

    @Multipart
    @POST("rating/add")
    suspend fun uploadFeedback(
        @Part("id_client") idClient: RequestBody,
        @Part("id_vendor") idVendor: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("message") message: RequestBody,
        @Part("rating") rating: RequestBody
    ): RatingResponse

    @Multipart
    @POST("rating/add")
    fun uploadFeedbackWithoutImage(
        @Part("id_client") idClient: RequestBody,
        @Part("id_vendor") idVendor: RequestBody,
        @Part("message") message: RequestBody,
        @Part("rating") rating: RequestBody
    ): Call<Void>


    @GET("vendor/filter")
    fun getVendorFilter(
        @Query("lokasi_kantor") lokasiKantor: String,
        @Query("tipe_layanan") tipeLayanan: String,
        @Query("jenis_jasa") jenisJasa: String,
        @Query("nama_vendor") namaVendor: String = ""
    ): Call<List<VendorResponseItem>>

}
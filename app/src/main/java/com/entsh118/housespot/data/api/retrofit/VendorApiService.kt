package com.entsh118.housespot.data.api.retrofit

import com.entsh118.housespot.data.api.response.UpdateUserResponse
import com.entsh118.housespot.data.api.response.UpdateVendorResponse
import com.entsh118.housespot.data.api.response.VendorAddResponse
import com.entsh118.housespot.data.api.response.VendorDetailsResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface VendorApiService {
    @Multipart
    @POST("add")
    suspend fun addVendor(
        @Part("id") id: RequestBody,
        @Part("jenis_properti") jenisProperti: RequestBody,
        @Part("lokasi_kantor") lokasiKantor: RequestBody,
        @Part("deskripsi_layanan") deskripsiLayanan: RequestBody,
        @Part("iklan_persetujuan") iklanPersetujuan: RequestBody,
        @Part("fee_minimum") feeMinimum: RequestBody,
        @Part portofolio: List<MultipartBody.Part>,
        @Part tipeLayanan: List<MultipartBody.Part>,
        @Part jasaKontraktor: List<MultipartBody.Part>
    ): VendorAddResponse

    @GET("{id_vendor}")
    fun getVendorById(
        @Path("id_vendor") idVendor: String
    ): Call<VendorDetailsResponse>

    @Multipart
    @PUT("{id_vendor}")
    suspend fun updateVendor(
        @Path("id_vendor") idVendor: String,
        @Part("id") id: RequestBody,
        @Part("jenis_properti") jenisProperti: RequestBody,
        @Part("lokasi_kantor") lokasiKantor: RequestBody,
        @Part("deskripsi_layanan") deskripsiLayanan: RequestBody,
        @Part("iklan_persetujuan") iklanPersetujuan: RequestBody,
        @Part("fee_minimum") feeMinimum: RequestBody,
        @Part portofolio: List<MultipartBody.Part>,
        @Part tipeLayanan: List<MultipartBody.Part>,
        @Part jasaKontraktor: List<MultipartBody.Part>
    ): UpdateVendorResponse
}


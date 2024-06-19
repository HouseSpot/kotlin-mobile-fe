package com.entsh118.housespot.data.api.retrofit

import com.entsh118.housespot.data.api.response.VendorAddResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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
}


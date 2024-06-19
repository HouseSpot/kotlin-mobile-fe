package com.entsh118.housespot.data.api.retrofit

import com.entsh118.housespot.data.api.model.UserPreferences
import com.entsh118.housespot.data.api.request.LoginRequest
import com.entsh118.housespot.data.api.response.LoginResponse
import com.entsh118.housespot.data.api.response.OrderDetailsResponse
import com.entsh118.housespot.data.api.response.RegisterResponse
import com.entsh118.housespot.data.api.response.VendorResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AuthApiService {
    @Multipart
    @POST("daftar")
    suspend fun register(
        @Part("email") email: RequestBody,
        @Part("nama") nama: RequestBody,
        @Part("no_hp") noHp: RequestBody,
        @Part("peran") peran: RequestBody,
        @Part("password") password: RequestBody,
        @Part("confirmPassword") confirmPassword: RequestBody,
        @Part profile: MultipartBody.Part
    ): RegisterResponse

    @POST("auth")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @GET("{id_user}")
    fun getUserProfile(
        @Path("id_user") idUser: String
    ): Call<UserPreferences>
}
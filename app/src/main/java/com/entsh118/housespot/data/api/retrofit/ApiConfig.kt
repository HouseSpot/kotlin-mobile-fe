package com.entsh118.housespot.data.api.retrofit

import com.entsh118.housespot.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    var BASE_URL: String? = BuildConfig.BASE_URL
    var BASE_URL_AUTH: String? = "${BASE_URL}users/"

    fun getAuthService(): AuthApiService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL_AUTH!!)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(AuthApiService::class.java)
    }

    fun getClientService(): ClientApiService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL!!)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ClientApiService::class.java)
    }

    fun getApiService(): ClientApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://cc-api-new-2na55gy4bq-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ClientApiService::class.java)
    }
}

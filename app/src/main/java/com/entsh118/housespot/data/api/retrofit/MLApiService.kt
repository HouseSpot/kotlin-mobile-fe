package com.entsh118.housespot.data.api.retrofit

import com.entsh118.housespot.data.api.request.PredictionRequest
import com.entsh118.housespot.data.api.response.PredictionResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MLApiService {
    @POST("/predict")
    suspend fun predict(
        @Body mlRequest: PredictionRequest
    ): PredictionResponse
}
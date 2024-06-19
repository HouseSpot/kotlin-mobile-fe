package com.entsh118.housespot.ui.prediksiharga.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entsh118.housespot.data.api.request.PredictionRequest
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject


class PrediksiFormViewModel : ViewModel() {

    private val _latitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> get() = _latitude

    private val _longitude = MutableLiveData<Double>()
    val longitude: LiveData<Double> get() = _longitude

    private val _predictionResult = MutableLiveData<String>()
    val predictionResult: LiveData<String> get() = _predictionResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun updateCoordinates(lat: Double, lon: Double) {
        _latitude.value = lat
        _longitude.value = lon
    }

    fun submitPrediction(predictionRequest: PredictionRequest) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = makeApiCall(predictionRequest)

                _loading.value = false
                _predictionResult.value = response
            } catch (e: Exception) {
                _loading.value = false
                _predictionResult.value = e.message ?: "Prediction failed"
            }
        }
    }

    private suspend fun makeApiCall(predictionRequest: PredictionRequest): String {

        val apiService = ApiConfig.getMLService()
        val response = apiService.predict(predictionRequest)
        return response.prediction ?: "Prediction failed"
    }
}

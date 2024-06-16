package com.entsh118.housespot.ui.auth.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.entsh118.housespot.data.api.request.LoginRequest
import com.entsh118.housespot.data.api.response.LoginResponse
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.data.api.model.UserPreferences
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _loginResult = MutableLiveData<LoginResponse>()
    val loginResult: LiveData<LoginResponse> = _loginResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val dataStoreManager = DataStoreManager(application)

    fun login(email: String, password: String) {
        _loading.value = true
        val loginRequest = LoginRequest(email, password)
        val apiService = ApiConfig.getAuthService()

        viewModelScope.launch {
            try {
                val response = apiService.login(loginRequest)
                _loginResult.value = response
                saveSession(response)
                _loading.value = false
            } catch (e: HttpException) {
                _error.value = parseErrorResponse(e)
                _loading.value = false
            } catch (e: IOException) {
                _error.value = "Network error, please check your connection"
                _loading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _loading.value = false
            }
        }
    }

    private suspend fun saveSession(loginResponse: LoginResponse) {
        val user = UserPreferences(
            id = loginResponse.data.id,
            email = loginResponse.data.email,
            nama = loginResponse.data.nama,
            profile = loginResponse.data.profile,
            noHp = loginResponse.data.no_hp,
            peran = loginResponse.data.peran,
            password = loginResponse.data.password
        )
        dataStoreManager.saveUserPreferences(user)
    }

    val userPreferencesFlow = dataStoreManager.userPreferencesFlow

    private fun parseErrorResponse(exception: HttpException): String {
        return try {
            val errorBody = exception.response()?.errorBody()?.string()
            val gson = Gson()
            val loginResponse = gson.fromJson(errorBody, LoginResponse::class.java)
            loginResponse.message
        } catch (e: Exception) {
            "An unknown error occurred"
        }
    }
}

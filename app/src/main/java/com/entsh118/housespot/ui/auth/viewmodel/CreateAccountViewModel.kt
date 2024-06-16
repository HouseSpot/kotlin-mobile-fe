package com.entsh118.housespot.ui.auth.viewmodel

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.Locale

class CreateAccountViewModel : ViewModel() {

    private val _registrationResult = MutableLiveData<String>()
    val registrationResult: LiveData<String> get() = _registrationResult

    private val _isRegistrationSuccess = MutableLiveData<Boolean>()
    val isRegistrationSuccess: LiveData<Boolean> get() = _isRegistrationSuccess

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        role: String,
        phone: String,
        profileImage: MultipartBody.Part
    ) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            _registrationResult.value = "Please fill all fields"
            return
        }
        if (password != confirmPassword) {
            _registrationResult.value = "Passwords do not match"
            return
        }

        _loading.value = true

        val requestBodyMap = hashMapOf(
            "email" to email.toRequestBody(MultipartBody.FORM),
            "nama" to name.toRequestBody(MultipartBody.FORM),
            "no_hp" to phone.toRequestBody(MultipartBody.FORM),
            "peran" to (role.lowercase() ?: "").toRequestBody(MultipartBody.FORM),
            "password" to password.toRequestBody(MultipartBody.FORM),
            "confirmPassword" to confirmPassword.toRequestBody(MultipartBody.FORM)
        )

        Log.d("CreateAccountViewModel", "register: $requestBodyMap")

        viewModelScope.launch {
            try {
                val response = ApiConfig.getAuthService().register(
                    requestBodyMap["email"]!!,
                    requestBodyMap["nama"]!!,
                    requestBodyMap["no_hp"]!!,
                    requestBodyMap["peran"]!!,
                    requestBodyMap["password"]!!,
                    requestBodyMap["confirmPassword"]!!,
                    profileImage
                )
                _registrationResult.value = response.message ?: "Registration successful"
                _isRegistrationSuccess.value = true
            } catch (e: Exception) {
                _registrationResult.value = e.message ?: "Registration failed"
                _isRegistrationSuccess.value = false
            } finally {
                _loading.value = false
            }
        }
    }
}

package com.entsh118.housespot.ui.account.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.data.api.response.UpdateVendorResponse
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File

class AccountEditPageViewModel(application: Application) : AndroidViewModel(application) {

    private val _updateResult = MutableLiveData<String>()
    val updateResult: LiveData<String> get() = _updateResult

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    private val _updateVendorResult = MutableLiveData<UpdateVendorResponse>()
    val updateVendorResult: LiveData<UpdateVendorResponse> get() = _updateVendorResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val dataStoreManager = DataStoreManager(application)

    fun updateUser(
        id: String,
        name: String,
        phone: String,
        profileImage: MultipartBody.Part? = null
    ) {
        if (name.isEmpty() || phone.isEmpty()) {
            _updateResult.value = "Please fill all fields"
            return
        }

        _loading.value = true

        val requestBodyMap = hashMapOf(
            "nama" to name.toRequestBody(MultipartBody.FORM),
            "no_hp" to phone.toRequestBody(MultipartBody.FORM),
        )

        Log.d("AccountEditPageVM", "update: $requestBodyMap")

        viewModelScope.launch {
            try {
                val response = if (profileImage != null) {
                    ApiConfig.getAuthService().updateUser(
                        id,
                        requestBodyMap["nama"]!!,
                        requestBodyMap["no_hp"]!!,
                        profileImage
                    )
                } else {
                    ApiConfig.getAuthService().updateUserWithoutImage(
                        id,
                        requestBodyMap["nama"]!!,
                        requestBodyMap["no_hp"]!!
                    )
                }
                _updateResult.value = response.message ?: "Update successful"
                _updateSuccess.value = true
                saveSession(name, phone, response.data?.profile)
                Log.d("AccountEditPageVM", "update: $response")

            } catch (e: Exception) {
                _updateResult.value = e.message ?: "Update failed"
                _updateSuccess.value = false

                Log.d("AccountEditPageVM", "update: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun updateVendor(
        id: String,
        tipeLayanan: List<String>,
        jenisProperti: String,
        jasaKontraktor: List<String>,
        lokasiKantor: String,
        deskripsiLayanan: String,
        iklanPersetujuan: Boolean,
        feeMinimum: String,
        portofolioPaths: List<String>
    ) {
        Log.d("AccountEditPageVM", "updating vendor")
        val idBody = id.toRequestBody("text/plain".toMediaTypeOrNull())
        val jenisPropertiBody = jenisProperti.toRequestBody("text/plain".toMediaTypeOrNull())
        val lokasiKantorBody = lokasiKantor.toRequestBody("text/plain".toMediaTypeOrNull())
        val deskripsiLayananBody = deskripsiLayanan.toRequestBody("text/plain".toMediaTypeOrNull())
        val feeMinimumBody = feeMinimum.toRequestBody("text/plain".toMediaTypeOrNull())
        val iklanPersetujuanBody = iklanPersetujuan.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val portofolioParts = portofolioPaths.map { path ->
            val file = File(path)
            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("portofolio", file.name, requestBody)
        }

        val tipeLayananParts = tipeLayanan.map {
            MultipartBody.Part.createFormData("tipe_layanan", it)
        }

        val jasaKontraktorParts = jasaKontraktor.map {
            MultipartBody.Part.createFormData("jasa_kontraktor", it)
        }


        Log.d("AccountEditPageVM", "updating vendor 2")

        _loading.value = true
        viewModelScope.launch {
            try {
                Log.d("AccountEditPageVM", "updating vendor 3")
                val response = ApiConfig.getVendorService().updateVendor(
                    id,
                    idBody,
                    jenisPropertiBody,
                    lokasiKantorBody,
                    deskripsiLayananBody,
                    iklanPersetujuanBody,
                    feeMinimumBody,
                    portofolioParts,
                    tipeLayananParts,
                    jasaKontraktorParts
                )
                _updateVendorResult.value = response

                Log.d("AccountEditPageVM", "updating vendor 4")
                _loading.value = false
                Log.d("AccountEditPageVM", "updateVendor: $response")

            } catch (e: Exception) {
                _loading.value = false
                Log.d("AccountEditPageVM", "updateVendor: ${e.message}")
                _updateVendorResult.value = UpdateVendorResponse(status = "error", message = e.message)
            }
        }
    }

    private suspend fun saveSession(nama:String, noHp:String, profileImage:String?) {
        dataStoreManager.updateUserPreferences(nama, noHp, profileImage)
    }
}

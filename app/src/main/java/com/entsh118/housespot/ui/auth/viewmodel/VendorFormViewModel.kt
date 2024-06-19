package com.entsh118.housespot.ui.auth.viewmodel

import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.entsh118.housespot.data.api.retrofit.ApiConfig
import com.entsh118.housespot.data.api.response.VendorAddResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class VendorFormViewModel : ViewModel() {

    private val _addVendorResult = MutableLiveData<VendorAddResponse>()
    val addVendorResult: LiveData<VendorAddResponse> get() = _addVendorResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _isFormValid = MutableLiveData<Boolean>()
    val isFormValid: LiveData<Boolean> get() = _isFormValid

    private val _termsAccepted = MutableLiveData<Boolean>()
    val termsAccepted: LiveData<Boolean> get() = _termsAccepted

    init {
        _isFormValid.value = false
    }

    fun validateForm(isValid: Boolean) {
        _isFormValid.value = isValid
    }

    fun updateTermsAccepted(isAccepted: Boolean) {
        _termsAccepted.value = isAccepted
    }

    fun addVendor(
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

        _loading.value = true
        viewModelScope.launch {
            try {
                val response = ApiConfig.getVendorService().addVendor(
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
                _addVendorResult.value = response
                _loading.value = false
            } catch (e: Exception) {
                _loading.value = false
                _addVendorResult.value = VendorAddResponse(status = "error", message = e.message)
            }
        }
    }
}

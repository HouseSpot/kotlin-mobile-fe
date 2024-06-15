package com.entsh118.housespot.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VendorFormViewModel : ViewModel() {

    private val _propertyType = MutableLiveData<String>()
    val propertyType: LiveData<String> get() = _propertyType

    private val _officeLocation = MutableLiveData<String>()
    val officeLocation: LiveData<String> get() = _officeLocation

    private val _serviceDescription = MutableLiveData<String>()
    val serviceDescription: LiveData<String> get() = _serviceDescription

    private val _termsAccepted = MutableLiveData<Boolean>()
    val termsAccepted: LiveData<Boolean> get() = _termsAccepted

    private val _isFormValid = MutableLiveData<Boolean>()
    val isFormValid: LiveData<Boolean> get() = _isFormValid

    init {
        _isFormValid.value = false
    }

    fun updatePropertyType(propertyType: String) {
        _propertyType.value = propertyType
        validateForm()
    }

    fun updateOfficeLocation(officeLocation: String) {
        _officeLocation.value = officeLocation
        validateForm()
    }

    fun updateServiceDescription(serviceDescription: String) {
        _serviceDescription.value = serviceDescription
        validateForm()
    }

    fun updateTermsAccepted(accepted: Boolean) {
        _termsAccepted.value = accepted
        validateForm()
    }

    private fun validateForm() {
        _isFormValid.value = !(_propertyType.value.isNullOrEmpty() ||
                _officeLocation.value.isNullOrEmpty() ||
                _serviceDescription.value.isNullOrEmpty() ||
                _termsAccepted.value != true)
    }
}

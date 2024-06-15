package com.entsh118.housespot.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CreateAccountViewModel : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: LiveData<String> get() = _confirmPassword

    private val _phone = MutableLiveData<String>()
    val phone: LiveData<String> get() = _phone

    private val _isFormValid = MutableLiveData<Boolean>()
    val isFormValid: LiveData<Boolean> get() = _isFormValid

    fun updateName(name: String) {
        _name.value = name
        validateForm()
    }

    fun updateEmail(email: String) {
        _email.value = email
        validateForm()
    }

    fun updatePassword(password: String) {
        _password.value = password
        validateForm()
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
        validateForm()
    }

    fun updatePhone(phone: String) {
        _phone.value = phone
        validateForm()
    }

    private fun validateForm() {
        _isFormValid.value = !(_name.value.isNullOrEmpty() ||
                _email.value.isNullOrEmpty() ||
                _password.value.isNullOrEmpty() ||
                _confirmPassword.value.isNullOrEmpty() ||
                _phone.value.isNullOrEmpty() ||
                _password.value != _confirmPassword.value)
    }
}

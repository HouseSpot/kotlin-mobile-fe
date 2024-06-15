package com.entsh118.housespot.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RoleSelectionViewModel : ViewModel() {
    private val _selectedRole = MutableLiveData<String?>()
    val selectedRole: LiveData<String?> get() = _selectedRole

    fun selectRole(role: String) {
        _selectedRole.value = role
    }
}

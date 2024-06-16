package com.entsh118.housespot.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.entsh118.housespot.data.DataStoreManager
import com.entsh118.housespot.data.api.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AccountViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStoreManager = DataStoreManager(application)

    val userPreferencesFlow: Flow<UserPreferences?>
        get() = dataStoreManager.userPreferencesFlow

    suspend fun logout() {
        dataStoreManager.clearUserPreferences()
    }
}

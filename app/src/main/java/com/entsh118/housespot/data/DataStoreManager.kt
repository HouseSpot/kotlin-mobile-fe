package com.entsh118.housespot.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.entsh118.housespot.data.api.model.UserPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class DataStoreManager(context: Context) {

    private val dataStore = context.dataStore

    companion object {
        val ID = stringPreferencesKey("id")
        val EMAIL = stringPreferencesKey("email")
        val NAMA = stringPreferencesKey("nama")
        val PROFILE = stringPreferencesKey("profile")
        val NO_HP = stringPreferencesKey("no_hp")
        val PERAN = stringPreferencesKey("peran")
        val PASSWORD = stringPreferencesKey("password")
    }

    suspend fun saveUserPreferences(user: UserPreferences) {
        dataStore.edit { preferences ->
            preferences[ID] = user.id
            preferences[EMAIL] = user.email
            preferences[NAMA] = user.nama
            preferences[PROFILE] = user.profile ?: ""
            preferences[NO_HP] = user.noHp
            preferences[PERAN] = user.peran
            preferences[PASSWORD] = user.password
        }
    }

    val userPreferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val id = preferences[ID] ?: ""
            val email = preferences[EMAIL] ?: ""
            val nama = preferences[NAMA] ?: ""
            val profile = preferences[PROFILE] ?: ""
            val noHp = preferences[NO_HP] ?: ""
            val peran = preferences[PERAN] ?: ""
            val password = preferences[PASSWORD] ?: ""
            UserPreferences(id, email, nama, profile, noHp, peran, password)
        }

    suspend fun clearUserPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}

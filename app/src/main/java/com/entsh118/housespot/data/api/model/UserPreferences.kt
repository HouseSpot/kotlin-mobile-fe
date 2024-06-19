package com.entsh118.housespot.data.api.model

data class UserPreferences(
    val id: String,
    val email: String,
    val nama: String,
    val profile: String?,
    val noHp: String,
    val peran: String,
    val password: String
)

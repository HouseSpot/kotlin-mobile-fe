package com.entsh118.housespot.data.api.response

data class ProfileResponse(
    val id: String,
    val email: String,
    val nama: String,
    val profile: String?,
    val no_hp: String,
    val peran: String,
    val password: String
)

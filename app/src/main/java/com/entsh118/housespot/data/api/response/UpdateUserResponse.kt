package com.entsh118.housespot.data.api.response

data class UpdateUserResponse (
    val message: String? = null,
    val status: String? = null,
    val data: UpdateData? = null
)

data class UpdateData (
    val password: String,
    val peran: String,
    val no_hp: String,
    val profile: String,
    val id: String,
    val nama: String
)
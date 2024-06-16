package com.entsh118.housespot.data.api.response

data class LoginResponse(
	val status: String,
	val message: String,
	val data: UserData
)

data class UserData(
	val password: String,
	val peran: String,
	val no_hp: String,
	val profile: String,
	val id: String,
	val email: String,
	val nama: String
)

package com.entsh118.housespot.data.api.response

import com.google.gson.annotations.SerializedName

data class PostOrderResponse(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

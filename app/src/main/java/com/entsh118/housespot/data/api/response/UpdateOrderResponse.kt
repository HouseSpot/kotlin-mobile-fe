package com.entsh118.housespot.data.api.response

import com.google.gson.annotations.SerializedName

data class UpdateOrderResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

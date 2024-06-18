package com.entsh118.housespot.data.api.response

import com.entsh118.housespot.data.api.model.Order
import com.google.gson.annotations.SerializedName

data class OrderByVendorResponse(

	@field:SerializedName("data")
	val data: List<Order?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

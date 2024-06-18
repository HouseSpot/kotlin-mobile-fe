package com.entsh118.housespot.data.api.response

import com.google.gson.annotations.SerializedName

data class OrderDetailsResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("serviceType")
	val serviceType: String? = null,

	@field:SerializedName("materialProvider")
	val materialProvider: String? = null,

	@field:SerializedName("id_pemesan")
	val idPemesan: String? = null,

	@field:SerializedName("endDate")
	val endDate: String? = null,

	@field:SerializedName("propertyType")
	val propertyType: String? = null,

	@field:SerializedName("projectDescription")
	val projectDescription: String? = null,

	@field:SerializedName("id_vendor")
	val idVendor: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("startDate")
	val startDate: String? = null,

	@field:SerializedName("budget")
	val budget: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

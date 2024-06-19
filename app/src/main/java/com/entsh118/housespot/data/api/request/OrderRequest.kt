package com.entsh118.housespot.data.api.request

import com.google.gson.annotations.SerializedName

data class OrderRequest(

	@field:SerializedName("serviceType")
	val serviceType: String,

	@field:SerializedName("materialProvider")
	val materialProvider: String,

	@field:SerializedName("id_pemesan")
	val idPemesan: String,

	@field:SerializedName("endDate")
	val endDate: String,

	@field:SerializedName("propertyType")
	val propertyType: String,

	@field:SerializedName("projectDescription")
	val projectDescription: String,

	@field:SerializedName("id_vendor")
	val idVendor: String,

	@field:SerializedName("startDate")
	val startDate: String,

	@field:SerializedName("budget")
	val budget: String
)

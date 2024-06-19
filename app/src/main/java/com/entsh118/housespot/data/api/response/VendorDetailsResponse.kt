package com.entsh118.housespot.data.api.response

import com.google.gson.annotations.SerializedName

data class VendorDetailsResponse(

	@field:SerializedName("tipe_layanan")
	val tipeLayanan: List<String?>? = null,

	@field:SerializedName("profile")
	val profile: String? = null,

	@field:SerializedName("rating")
	val rating: Double? = null,

	@field:SerializedName("deskripsi_layanan")
	val deskripsiLayanan: String? = null,

	@field:SerializedName("iklan_persetujuan")
	val iklanPersetujuan: Boolean? = null,

	@field:SerializedName("pemilik_info")
	val pemilikInfo: PemilikInfo? = null,

	@field:SerializedName("portofolio")
	val portofolio: List<String?>? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("jenis_properti")
	val jenisProperti: String? = null,

	@field:SerializedName("lokasi_kantor")
	val lokasiKantor: String? = null,

	@field:SerializedName("jasa_kontraktor")
	val jasaKontraktor: List<String?>? = null,

	@field:SerializedName("fee_minimum")
	val feeMinimum: String? = null
)


package com.entsh118.housespot.data.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class VendorResponse(

	@field:SerializedName("VendorResponse")
	val vendorResponse: List<VendorResponseItem>? =  emptyList(),
)

@Parcelize
data class PemilikInfo(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("no_hp")
	val noHp: String? = null,

	@field:SerializedName("email")
	val email: String? = null
):Parcelable


@Parcelize
data class VendorResponseItem(

	@field:SerializedName("tipe_layanan")
	val tipeLayanan: List<String?>? = null,

	@field:SerializedName("profile")
	val profile: String? = null,

	@field:SerializedName("rating")
	val rating: Integer? = null,

	@field:SerializedName("deskripsi_layanan")
	val deskripsiLayanan: String? = null,

	@field:SerializedName("iklan_persetujuan")
	val iklanPersetujuan: String? = null,

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
	val jasaKontraktor: List<String?>? = null
): Parcelable

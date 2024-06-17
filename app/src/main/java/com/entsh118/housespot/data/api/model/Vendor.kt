package com.entsh118.housespot.data.api.model

data class Vendor(
    val tipeLayanan: List<String?>,
    val profile: String,
    val rating: Any,
    val deskripsiLayanan: String,
    val iklanPersetujuan: String,
    val pemilikInfo: PemilikInfo,
    val portofolio: List<String?>,
    val id: String,
    val jenisProperti: String,
    val lokasiKantor: String,
    val jasaKontraktor: List<String?>
) {
    constructor() : this(
        listOf("Service Type"),
        "Profile",
        0.0,
        "Service Description",
        "Approval",
        PemilikInfo(),
        listOf("Portfolio Item"),
        "ID",
        "Property Type",
        "Office Location",
        listOf("Contractor Service")
    )
}

data class PemilikInfo(
    val nama: String,
    val noHp: String,
    val email: String
) {
    constructor() : this("Owner Name", "Owner Phone", "Owner Email")
}


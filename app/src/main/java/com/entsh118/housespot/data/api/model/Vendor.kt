package com.entsh118.housespot.data.api.model

data class Vendor(
    val tipeLayanan: List<String?>,
    val profile: String,
    val rating: Double,
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

    companion object {

        var sortByAscRating = Comparator<Vendor> { o1, o2 -> o1.rating.toInt() - o2.rating.toInt() }
        var sortByDescRating = Comparator<Vendor> { o1, o2 -> o2.rating.toInt()- o1.rating.toInt() }
    }

}

data class PemilikInfo(
    val nama: String,
    val noHp: String,
    val email: String
) {
    constructor() : this("Owner Name", "Owner Phone", "Owner Email")
}


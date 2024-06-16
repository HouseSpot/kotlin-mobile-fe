package com.entsh118.housespot.data.api.model

data class Order(
    val id: String,
    val id_pemesan: String,
    val id_vendor: String,
    val projectDescription: String,
    val serviceType: String,
    val propertyType: String,
    val materialProvider: String,
    val startDate: String,
    val endDate: String,
    val budget: Long,
    val status: String
) {
    constructor() : this("123", "234", "456",
        "Project Description", "Service Type", "Property Type",
        "Material Provider", "Start Date", "End Date", 1000000, "Status")
}


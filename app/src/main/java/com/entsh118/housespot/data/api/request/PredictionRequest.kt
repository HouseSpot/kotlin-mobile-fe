package com.entsh118.housespot.data.api.request

data class PredictionRequest(
    val city: String,
    val district: String,
    val latitude: Double,
    val longitude: Double,
    val landSize: Double,
    val buildingSize: Double,
    val floors: Int,
    val bedrooms: Int,
    val bathrooms: Int,
    val carportGarage: Int
)
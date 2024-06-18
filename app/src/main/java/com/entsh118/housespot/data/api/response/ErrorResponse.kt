package com.entsh118.housespot.data.api.response

import com.google.gson.annotations.SerializedName


data class ErrorResponse(
    @field:SerializedName("message")
    val message: String
)
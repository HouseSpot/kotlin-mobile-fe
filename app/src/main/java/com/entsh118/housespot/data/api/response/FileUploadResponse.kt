package com.entsh118.housespot.data.api.response

import com.google.gson.annotations.SerializedName

data class FileUploadResponse (
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("message")
    val message: String
)
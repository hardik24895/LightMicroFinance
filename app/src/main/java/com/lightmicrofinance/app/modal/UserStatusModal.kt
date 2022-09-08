package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName

data class UserStatusModal(

    @field:SerializedName("data")
    val data: StatusData? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class StatusData(

    @field:SerializedName("Status")
    val status: String? = null
)

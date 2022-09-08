package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName

data class ReasonListModal(

    @field:SerializedName("data")
    val data: List<ReasonDataItem> = mutableListOf(),

    @field:SerializedName("rows")
    val rows: Int? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ReasonDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("ReasonID")
    val reasonID: String? = null,

    @field:SerializedName("Reason")
    val reason: String? = null,

    @field:SerializedName("IsPOT")
    val isPOT: String? = null


)

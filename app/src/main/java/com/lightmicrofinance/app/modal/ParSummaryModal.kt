package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName

data class ParSummaryModal(

    @field:SerializedName("data")
    val data: ParSummaryData? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class JsonMember3160(

    @field:SerializedName("OverdueAmount")
    val overdueAmount: String? = null,

    @field:SerializedName("Bucket")
    val bucket: String? = null,

    @field:SerializedName("Amount")
    val amount: String? = null,

    @field:SerializedName("Clients")
    val clients: String? = null
)

data class JsonMember6190(

    @field:SerializedName("OverdueAmount")
    val overdueAmount: String? = null,

    @field:SerializedName("Bucket")
    val bucket: String? = null,

    @field:SerializedName("Amount")
    val amount: String? = null,

    @field:SerializedName("Clients")
    val clients: String? = null
)

data class JsonMember91180(

    @field:SerializedName("OverdueAmount")
    val overdueAmount: String? = null,

    @field:SerializedName("Bucket")
    val bucket: String? = null,

    @field:SerializedName("Amount")
    val amount: String? = null,

    @field:SerializedName("Clients")
    val clients: String? = null
)

data class JsonMember130(

    @field:SerializedName("OverdueAmount")
    val overdueAmount: String? = null,

    @field:SerializedName("Bucket")
    val bucket: String? = null,

    @field:SerializedName("Amount")
    val amount: String? = null,

    @field:SerializedName("Clients")
    val clients: String? = null
)

data class ParSummaryData(

    @field:SerializedName("180+")
    val jsonMember180: JsonMember180? = null,

    @field:SerializedName("61-90")
    val jsonMember6190: JsonMember6190? = null,

    @field:SerializedName("31-60")
    val jsonMember3160: JsonMember3160? = null,

    @field:SerializedName("1-30")
    val jsonMember130: JsonMember130? = null,

    @field:SerializedName("91-180")
    val jsonMember91180: JsonMember91180? = null,

    @field:SerializedName("Total")
    val total: Total? = null
)

data class JsonMember180(

    @field:SerializedName("OverdueAmount")
    val overdueAmount: String? = null,

    @field:SerializedName("Bucket")
    val bucket: String? = null,

    @field:SerializedName("Amount")
    val amount: String? = null,

    @field:SerializedName("Clients")
    val clients: String? = null
)


data class Total(

    @field:SerializedName("OverdueAmount")
    val overdueAmount: String? = null,

    @field:SerializedName("Bucket")
    val bucket: String? = null,

    @field:SerializedName("Amount")
    val amount: String? = null,

    @field:SerializedName("Clients")
    val clients: String? = null
)
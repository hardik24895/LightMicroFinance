package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName

data class CollectionSummaryModal(

    @field:SerializedName("data")
    val data: List<CollectionSummaryDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Response(

    @field:SerializedName("Target")
    val target: String? = null,

    @field:SerializedName("Percentage")
    val percentage: String? = null,

    @field:SerializedName("Clients")
    val clients: String? = null,

    @field:SerializedName("Collected")
    val collected: String? = null,

    @field:SerializedName("Pending")
    val pending: String? = null
)

data class CollectionSummaryDataItem(

    @field:SerializedName("Response")
    val response: Response? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("Title")
    val title: String? = null
)

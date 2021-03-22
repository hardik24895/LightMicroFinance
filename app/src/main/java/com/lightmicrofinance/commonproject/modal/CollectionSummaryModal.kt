package com.lightmicrofinance.commonproject.modal

import com.google.gson.annotations.SerializedName

data class CollectionSummaryModal(

    @field:SerializedName("data")
    val data: List<CollectionSummaryDataItem> = mutableListOf(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class All(

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

data class Partialy(

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

data class Pending(

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

data class Collected(

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

    @field:SerializedName("All")
    val all: All? = null,

    @field:SerializedName("Partialy")
    val partialy: Partialy? = null,

    @field:SerializedName("Pending")
    val pending: Pending? = null,

    @field:SerializedName("Collected")
    val collected: Collected? = null
)

package com.lightmicrofinance.commonproject.modal

import com.google.gson.annotations.SerializedName

data class CollectionSummaryReportModal(

    @field:SerializedName("data")
    val data: ollectionSummaryReportData? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Collection(

    @field:SerializedName("Total")
    val total: String? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("Collected")
    val collected: String? = null,

    @field:SerializedName("Pending")
    val pending: String? = null,

    @field:SerializedName("Partialy")
    val partialy: String? = null
)

data class Client(

    @field:SerializedName("Total")
    val total: String? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("Collected")
    val collected: String? = null,

    @field:SerializedName("Pending")
    val pending: String? = null,

    @field:SerializedName("Partialy")
    val partialy: String? = null
)

data class Demand(

    @field:SerializedName("Total")
    val total: String? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("Collected")
    val collected: String? = null,

    @field:SerializedName("Pending")
    val pending: String? = null,

    @field:SerializedName("Partialy")
    val partialy: String? = null
)

data class ollectionSummaryReportData(

    @field:SerializedName("Demand")
    val demand: Demand? = null,

    @field:SerializedName("Percentage")
    val percentage: Percentage? = null,

    @field:SerializedName("Collection")
    val collection: Collection? = null,

    @field:SerializedName("Client")
    val client: Client? = null,

    @field:SerializedName("Pending")
    val pending: Pending? = null
)

data class Percentage(

    @field:SerializedName("Total")
    val total: String? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("Collected")
    val collected: String? = null,

    @field:SerializedName("Pending")
    val pending: String? = null,

    @field:SerializedName("Partialy")
    val partialy: String? = null
)

data class Pending(

    @field:SerializedName("Total")
    val total: String? = null,

    @field:SerializedName("Title")
    val title: String? = null,

    @field:SerializedName("Collected")
    val collected: String? = null,

    @field:SerializedName("Pending")
    val pending: String? = null,

    @field:SerializedName("Partialy")
    val partialy: String? = null
)

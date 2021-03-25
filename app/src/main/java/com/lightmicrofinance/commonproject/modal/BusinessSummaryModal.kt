package com.lightmicrofinance.commonproject.modal

import com.google.gson.annotations.SerializedName

data class BusinessSummaryModal(

	@field:SerializedName("data")
	val data: BusinessSummaryData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class GRT(

    @field:SerializedName("Diff")
    val diff: String? = null,

    @field:SerializedName("Percentage")
    val percentage: String? = null,

    @field:SerializedName("GRT")
    val gRT: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("AchGRT")
    val achGRT: String? = null
)

data class DDDone(

    @field:SerializedName("Diff")
    val diff: String? = null,

    @field:SerializedName("Percentage")
    val percentage: String? = null,

    @field:SerializedName("DDDone")
    val dDDone: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("AchDDDone")
    val achDDDone: String? = null
)

data class DisbClient(

    @field:SerializedName("AchDisbClient")
    val achDisbClient: String? = null,

    @field:SerializedName("Diff")
    val diff: String? = null,

    @field:SerializedName("Percentage")
    val percentage: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("DisbClient")
    val disbClient: String? = null
)

data class BusinessSummaryData(

	@field:SerializedName("DisbAmount")
	val disbAmount: DisbAmount? = null,

	@field:SerializedName("DDPositive")
	val dDPositive: DDPositive? = null,

	@field:SerializedName("LENew")
	val lENew: LENew? = null,

	@field:SerializedName("TotalLE")
	val totalLE: TotalLE? = null,

	@field:SerializedName("GRT")
	val gRT: GRT? = null,

	@field:SerializedName("DDDone")
	val dDDone: DDDone? = null,

	@field:SerializedName("DisbClient")
	val disbClient: DisbClient? = null,

	@field:SerializedName("LERenew")
	val lERenew: LERenew? = null
)

data class TotalLE(

    @field:SerializedName("Diff")
    val diff: String? = null,

    @field:SerializedName("Percentage")
    val percentage: String? = null,

    @field:SerializedName("TotalLE")
    val totalLE: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("AchTotalLE")
    val achTotalLE: String? = null


)

data class LERenew(

    @field:SerializedName("Diff")
    val diff: String? = null,

    @field:SerializedName("Percentage")
    val percentage: String? = null,

    @field:SerializedName("LERenew")
    val lERenew: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("AchLERenew")
    val achLERenew: String? = null
)

data class DDPositive(

    @field:SerializedName("AchDDPositive")
    val achDDPositive: String? = null,

    @field:SerializedName("DDPositive")
    val dDPositive: String? = null,

    @field:SerializedName("Diff")
    val diff: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("Percentage")
    val percentage: String? = null
)

data class DisbAmount(

    @field:SerializedName("DisbAmount")
    val disbAmount: String? = null,

    @field:SerializedName("Diff")
    val diff: String? = null,

    @field:SerializedName("Percentage")
    val percentage: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("AchDisbAmount")
    val achDisbAmount: String? = null
)

data class LENew(

    @field:SerializedName("Diff")
    val diff: String? = null,

    @field:SerializedName("Percentage")
    val percentage: String? = null,

    @field:SerializedName("LENew")
    val lENew: String? = null,

    @field:SerializedName("Color")
    val color: String? = null,

    @field:SerializedName("AchLENew")
    val achLENew: String? = null
)

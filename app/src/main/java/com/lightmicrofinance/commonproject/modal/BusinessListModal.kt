package com.lightmicrofinance.commonproject.modal

import com.google.gson.annotations.SerializedName

data class BusinessListModal(

	@field:SerializedName("data")
	val data: List<BusinessListDataItem> = mutableListOf(),

	@field:SerializedName("rows")
	val rows: Int = 0,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class BusinessListDataItem(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("AchDDPositive")
	val achDDPositive: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("DDPositive")
	val dDPositive: String? = null,

	@field:SerializedName("LENew")
	val lENew: String? = null,

	@field:SerializedName("GRT")
	val gRT: String? = null,

	@field:SerializedName("DDDone")
	val dDDone: String? = null,

	@field:SerializedName("DisbClient")
	val disbClient: String? = null,

	@field:SerializedName("AchTotalLE")
	val achTotalLE: String? = null,

	@field:SerializedName("AchDDDone")
	val achDDDone: String? = null,

	@field:SerializedName("AchGRT")
	val achGRT: String? = null,

	@field:SerializedName("Date")
	val date: String? = null,

	@field:SerializedName("AchDisbAmount")
	val achDisbAmount: String? = null,

	@field:SerializedName("AchLERenew")
	val achLERenew: String? = null,

	@field:SerializedName("DisbAmount")
	val disbAmount: String? = null,

	@field:SerializedName("AchDisbClient")
	val achDisbClient: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("FECode")
	val fECode: String? = null,

	@field:SerializedName("Branch")
	val branch: String? = null,

	@field:SerializedName("TotalLE")
	val totalLE: String? = null,

	@field:SerializedName("AchLENew")
	val achLENew: String? = null,

	@field:SerializedName("LERenew")
	val lERenew: String? = null
)

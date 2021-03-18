package com.lightmicrofinance.commonproject.modal

import com.google.gson.annotations.SerializedName

data class ParListModal(

	@field:SerializedName("data")
	val data: List<ParDataItem> = mutableListOf(),

	@field:SerializedName("rows")
	val rows: Int = 0,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ParDataItem(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("OverdueAmount")
	val overdueAmount: String? = null,

	@field:SerializedName("CreatedBy")
	val createdBy: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("Amount")
	val amount: String? = null,

	@field:SerializedName("ParID")
	val parID: String? = null,

	@field:SerializedName("ModifiedBy")
	val modifiedBy: String? = null,

	@field:SerializedName("ModifiedDate")
	val modifiedDate: String? = null,

	@field:SerializedName("CenterName")
	val centerName: String? = null,

	@field:SerializedName("LoanID")
	val loanID: String? = null,

	@field:SerializedName("ClientName")
	val clientName: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("FECode")
	val fECode: String? = null,

	@field:SerializedName("Bucket")
	val bucket: String? = null,

	@field:SerializedName("Branch")
	val branch: String? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("ClientID")
	val clientID: String? = null,

	@field:SerializedName("DPD")
	val dPD: String? = null
)

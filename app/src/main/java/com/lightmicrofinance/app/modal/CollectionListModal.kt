package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CollectionListModal(

	@field:SerializedName("data")
	val data: List<CollectionDataItem> = mutableListOf(),

	@field:SerializedName("rows")
	val rows: Int = 0,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CollectionDataItem(

	@field:SerializedName("ModifiedBy")
	val modifiedBy: String? = null,

	@field:SerializedName("ModifiedDate")
	val modifiedDate: String? = null,

	@field:SerializedName("LoanID")
	val loanID: String? = null,

	@field:SerializedName("ClientName")
	val clientName: String? = null,

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("FECode")
	val fECode: String? = null,

	@field:SerializedName("CollectionID")
	val collectionID: String? = null,

	@field:SerializedName("Branch")
	val branch: String? = null,

	@field:SerializedName("CenterID")
	val centerID: String? = null,

	@field:SerializedName("DueDate")
	val dueDate: String? = null,

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("CurrentDemand")
	val currentDemand: String? = null,

	@field:SerializedName("CreatedBy")
	val createdBy: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("Percentage")
	val percentage: String? = null,

	@field:SerializedName("RegularCollection")
	val regularCollection: String? = null,

	@field:SerializedName("AdvanceCollection")
	val advanceCollection: String? = null,

	@field:SerializedName("OriginalDemand")
	val originalDemand: String? = null,

	@field:SerializedName("CenterName")
	val centerName: String? = null,

	@field:SerializedName("OriginalCollection")
	val originalCollection: String? = null,

	@field:SerializedName("TodaysCollection")
	val todaysCollection: String? = null,

	@field:SerializedName("TotalCollection")
	val totalCollection: String? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("ClientID")
	val clientID: String? = null,

	@field:SerializedName("Pending")
	val pending: String? = null

) : Serializable

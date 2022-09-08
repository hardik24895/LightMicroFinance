package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName

data class CollectionChartModal(

	@field:SerializedName("data")
	val data: List<CollectionChartDataItem> = mutableListOf(),

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CollectionChartDataItem(

	@field:SerializedName("TotalCollection")
	val totalCollection: String? = null,

	@field:SerializedName("OriginalDemand")
	val originalDemand: String? = null,

	@field:SerializedName("DueDate")
	val dueDate: String? = null
)

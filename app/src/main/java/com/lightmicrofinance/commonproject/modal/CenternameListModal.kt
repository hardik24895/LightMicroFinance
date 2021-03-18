package com.lightmicrofinance.commonproject.modal

import com.google.gson.annotations.SerializedName

data class CenternameListModal(

	@field:SerializedName("data")
	val data: List<CenternameDataItem> = mutableListOf(),

	@field:SerializedName("rows")
	val rows: Int? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CenternameDataItem(

	@field:SerializedName("CenterName")
	val centerName: String? = null
)

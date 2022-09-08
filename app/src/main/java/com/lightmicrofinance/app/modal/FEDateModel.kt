package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName

data class FEDateModel(

	@field:SerializedName("data")
	val data: List<FEDataItem> = mutableListOf(),

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class FEDataItem(

	@field:SerializedName("Rno")
	val rno: String? = null,

	@field:SerializedName("FECode")
	val fECode: String? = null,

	@field:SerializedName("rowcount")
	val rowcount: String? = null,

	@field:SerializedName("BMCode")
	val bMCode: String? = null,

	@field:SerializedName("Name")
	val name: String? = null
)

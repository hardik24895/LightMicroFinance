package com.lightmicrofinance.commonproject.modal

import com.google.gson.annotations.SerializedName

data class CommanIDModal(

	@field:SerializedName("data")
	val data: CommanIDData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CommanIDData(

	@field:SerializedName("ID")
	val iD: String? = null
)

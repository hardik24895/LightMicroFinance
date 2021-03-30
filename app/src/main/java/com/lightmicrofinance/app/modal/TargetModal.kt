package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName

data class TargetModal(

	@field:SerializedName("data")
	val data: TargetData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class TargetData(

	@field:SerializedName("Target")
	val target: String? = null,

	@field:SerializedName("Percentage")
	val percentage: String? = null,

	@field:SerializedName("Collected")
	val collected: String? = null
)

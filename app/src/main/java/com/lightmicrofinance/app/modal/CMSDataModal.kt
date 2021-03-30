package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName

data class CMSDataModal(

	@field:SerializedName("data")
	val data: CMSData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class CMSData(

	@field:SerializedName("CMSID")
	val cMSID: String? = null,

	@field:SerializedName("PageID")
	val pageID: String? = null,

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("Content")
	val content: String? = null,

	@field:SerializedName("PageName")
	val pageName: String? = null
)

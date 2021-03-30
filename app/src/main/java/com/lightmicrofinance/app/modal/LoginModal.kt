package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName

data class LoginModal(

	@field:SerializedName("data")
	val data: LoginData? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class LoginData(

	@field:SerializedName("Status")
	val status: String? = null,

	@field:SerializedName("Designation")
	val designation: String? = null,

	@field:SerializedName("CreatedBy")
	val createdBy: String? = null,

	@field:SerializedName("RMName")
	val rMName: String? = null,

	@field:SerializedName("ModifiedBy")
	val modifiedBy: Any? = null,

	@field:SerializedName("ModifiedDate")
	val modifiedDate: Any? = null,

	@field:SerializedName("Mobile")
	val mobile: String? = null,

	@field:SerializedName("Name")
	val name: String? = null,

	@field:SerializedName("CenterName")
	val centerName: String? = null,

	@field:SerializedName("FECode")
	val fECode: String? = null,

	@field:SerializedName("State")
	val state: String? = null,

	@field:SerializedName("CreatedDate")
	val createdDate: String? = null,

	@field:SerializedName("FEID")
	val fEID: String? = null,

	@field:SerializedName("AMName")
	val aMName: String? = null,

	@field:SerializedName("BranchCode")
	val branchCode: String? = null,

	@field:SerializedName("Password")
	val password: String? = null
)

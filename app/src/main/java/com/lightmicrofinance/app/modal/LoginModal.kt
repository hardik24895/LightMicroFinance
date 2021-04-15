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

	@field:SerializedName("Designation")
	val designation: String? = null,

	@field:SerializedName("BMCode")
	val bMCode: String? = null,

	@field:SerializedName("RMName")
	val rMName: String? = null,

	@field:SerializedName("Mobile")
	val mobile: String? = null,

	@field:SerializedName("FEName")
	val fEName: String? = null,

	@field:SerializedName("CenterName")
	val centerName: String? = null,

	@field:SerializedName("FECode")
	val fECode: String? = null,

	@field:SerializedName("UserID")
	val userID: String? = null,

	@field:SerializedName("BMName")
	val bMName: String? = null,

	@field:SerializedName("BMID")
	val bMID: String? = null,

	@field:SerializedName("FEID")
	val fEID: String? = null,

	@field:SerializedName("AMName")
	val aMName: String? = null,

	@field:SerializedName("CenterID")
	val centerID: String? = null,

	@field:SerializedName("UserType")
	val userType: String? = null,

	@field:SerializedName("BranchCode")
	val branchCode: String? = null,

	@field:SerializedName("Password")
	val password: String? = null
)

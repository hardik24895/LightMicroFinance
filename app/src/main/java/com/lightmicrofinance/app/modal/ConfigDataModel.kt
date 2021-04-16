package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName

data class ConfigDataModel(

    @field:SerializedName("data")
    val data: ConfigData? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class ConfigData(

    @field:SerializedName("MailFromName")
    val mailFromName: String? = null,

    @field:SerializedName("AppVersionAndroid")
    val appVersionAndroid: String? = null,

    @field:SerializedName("TimeZone")
    val timeZone: String? = null,

    @field:SerializedName("AppVersionIOS")
    val appVersionIOS: String? = null,

    @field:SerializedName("SupportEmail")
    val supportEmail: String? = null,

    @field:SerializedName("EmailPassword")
    val emailPassword: String? = null,

    @field:SerializedName("ConfigID")
    val configID: String? = null
)

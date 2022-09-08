package com.lightmicrofinance.app.modal

import com.google.gson.annotations.SerializedName

data class GoalsheetModal(

    @field:SerializedName("data")
    val data: List<GoalsheetDataItem> = mutableListOf(),

    @field:SerializedName("rows")
    val rows: Int = 0,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class GoalsheetDataItem(

    @field:SerializedName("Status")
    val status: String? = null,

    @field:SerializedName("ClosedClients")
    val closedClients: String? = null,

    @field:SerializedName("IsCurrentMonth")
    val isCurrentMonth: String? = null,

    @field:SerializedName("CreatedBy")
    val createdBy: String? = null,

    @field:SerializedName("rowcount")
    val rowcount: String? = null,

    @field:SerializedName("AchOpeningClients")
    val achOpeningClients: String? = null,

    @field:SerializedName("AchTotalDis")
    val achTotalDis: String? = null,

    @field:SerializedName("CollectionPerc")
    val collectionPerc: String? = null,

    @field:SerializedName("AchClosedClients")
    val achClosedClients: String? = null,

    @field:SerializedName("ModifiedBy")
    val modifiedBy: String? = null,

    @field:SerializedName("ModifiedDate")
    val modifiedDate: String? = null,

    @field:SerializedName("RenewDis")
    val renewDis: String? = null,

    @field:SerializedName("ClosingClient")
    val closingClient: String? = null,

    @field:SerializedName("Month")
    val month: String? = null,

    @field:SerializedName("AchRenewDis")
    val achRenewDis: String? = null,

    @field:SerializedName("AchCollectionPerc")
    val achCollectionPerc: String? = null,

    @field:SerializedName("Rno")
    val rno: String? = null,

    @field:SerializedName("FECode")
    val fECode: String? = null,

    @field:SerializedName("OpeningClients")
    val openingClients: String? = null,

    @field:SerializedName("Branch")
    val branch: String? = null,

    @field:SerializedName("AchClosingClient")
    val achClosingClient: String? = null,

    @field:SerializedName("NewClients")
    val newClients: String? = null,

    @field:SerializedName("CreatedDate")
    val createdDate: String? = null,

    @field:SerializedName("AchNewClients")
    val achNewClients: String? = null,

    @field:SerializedName("GoalID")
    val goalID: String? = null,

    @field:SerializedName("TotalDis")
    val totalDis: String? = null


)

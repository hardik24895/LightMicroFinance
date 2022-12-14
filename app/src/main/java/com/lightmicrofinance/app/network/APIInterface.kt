package com.lightmicrofinance.app.network


import com.lightmicrofinance.app.modal.*
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIInterface {

    @POST("user/login")
    fun login(@Body body: RequestBody): Observable<Response<LoginModal>>

    @POST("user/resetPassword")
    fun resetPassword(@Body body: RequestBody): Observable<Response<LoginModal>>

    @POST("collection/getCollectionSummery")
    fun getCollectionSummary(@Body body: RequestBody): Observable<Response<CollectionSummaryModal>>

    @POST("collection/getChart")
    fun getCollectionChart(@Body body: RequestBody): Observable<Response<CollectionChartModal>>

    @POST("collection/getTarget")
    fun getTarget(@Body body: RequestBody): Observable<Response<TargetModal>>

    @POST("user/changePassword")
    fun changePassword(@Body body: RequestBody): Observable<Response<LoginModal>>

    /* @Multipart
     @POST("service/")
     fun AddCustomerSiteDocument(
         @Part ImageData: MultipartBody.Part,
         @Part("method") method: RequestBody,
         @Part("UserID") UserID: RequestBody,
         @Part("Title") Title: RequestBody,
         @Part("SitesID") Description: RequestBody
     ): Observable<Response<CommonAddModal>>*/

    @POST("collection/getCollection")
    fun getCollection(@Body body: RequestBody): Observable<Response<CollectionListModal>>

    @POST("par/getPar")
    fun getPar(@Body body: RequestBody): Observable<Response<ParListModal>>

    @POST("business/getBusiness")
    fun getBusiness(@Body body: RequestBody): Observable<Response<BusinessListModal>>

    @POST("collection/GetCenters")
    fun getCenterName(@Body body: RequestBody): Observable<Response<CenternameListModal>>

    @POST("par/GetCenters")
    fun getParCenterName(@Body body: RequestBody): Observable<Response<CenternameListModal>>


    @POST("cms/getPage")
    fun getCMS(@Body body: RequestBody): Observable<Response<CMSDataModal>>

    @POST("business/getSummery")
    fun getBusinessSammary(@Body body: RequestBody): Observable<Response<BusinessSummaryModal>>

    @POST("par/getParSummery")
    fun getParSummary(@Body body: RequestBody): Observable<Response<ParSummaryModal>>

    @POST("collection/getCollectionSummeryInverted")
    fun getCollectionSummaryReport(@Body body: RequestBody): Observable<Response<CollectionSummaryReportModal>>

    @POST("reason/getReasons")
    fun getReason(@Body body: RequestBody): Observable<Response<ReasonListModal>>

    @POST("collection/editCollection")
    fun addCollection(@Body body: RequestBody): Observable<Response<CommanIDModal>>

    @POST("goalsheet/getGoalSheet")
    fun getGoalSheet(@Body body: RequestBody): Observable<Response<GoalsheetModal>>

    @POST("user/getFeList")
    fun getFEList(@Body body: RequestBody): Observable<Response<FEDateModel>>

    @POST("user/checkUserStatus")
    fun checkUserStatus(@Body body: RequestBody): Observable<Response<UserStatusModal>>

    @POST("config/getConfig")
    fun getConfig(@Body body: RequestBody): Observable<Response<ConfigDataModel>>


}

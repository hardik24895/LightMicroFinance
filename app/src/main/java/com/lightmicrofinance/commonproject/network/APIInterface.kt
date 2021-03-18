package com.commonProject.network


import com.lightmicrofinance.commonproject.modal.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface APIInterface {

    @POST("user/login")
    fun login(@Body body: RequestBody): Observable<Response<LoginModal>>

    @POST("user/resetPassword")
    fun resetPassword(@Body body: RequestBody): Observable<Response<LoginModal>>

    @POST("collection/getTarget")
    fun getTarget(@Body body: RequestBody): Observable<Response<TargetModal>>

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

    @POST("collection/GetCenters")
    fun getCenterName(@Body body: RequestBody): Observable<Response<CenternameListModal>>

    @POST("cms/getPage")
    fun getCMS(@Body body: RequestBody): Observable<Response<CMSDataModal>>
}

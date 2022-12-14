package com.lightmicrofinance.app.utils

import com.lightmicrofinance.app.BuildConfig

object Constant {
    const val NA = "NA"
    const val USER_TYPE = "user_type"
    const val BM = "BM"
    const val FE = "FE"
    const val ISCHECKED = "isChecked"
    const val LEAD = "lead"
    const val PAR = "par"
    const val COLLECTION = "collection"
    const val BUSINESS = "business"
    const val BUCKET = "bucket"
    const val BUCKET_SIZE = "bucket_size"
    const val BUSINESS_SUMMARY = "business_summary"
    const val PAR_SUMMARY = "par_summary"
    const val COLLECTION_SUMMARY = "collection_summary"
    const val PENDING = "pending"

    //   const val PARTIALY = "Partialy"
    const val ALL = "All"
    const val COLLECTED = "Collected"
    const val PAYMENT = "Payment"
    const val TYPE = "type"
    const val PASSWORD = "password"
    const val SITE = "site"
    const val TICKET = "ticket"
    const val CUSTOMER = "customer"
    const val EMPLOYEE = "employee"
    const val VISITOR_ID = "visitor_id"
    const val CUSTOMER_ID = "customer_id"
    const val CUSTOMER_NAME = "customer_name"

    // const val BASE_URL = "http://societyfy.in/lightmf/"
    // const val BASE_URL = "http://societyfy.in/lightmf_stagging/"

    const val BASE_URL = BuildConfig.SERVER_URL
    const val API_URL = "${BASE_URL}api/"
    const val APK_DOWNLOAD = "${BASE_URL}android/lmf.apk"
    const val EMP_PROFILE = "${BASE_URL}assets/uploads/user/"
    const val TICKET_IMG = "${BASE_URL}assets/uploads/ticket/"
    const val PDF_INVOICE_URL = "${BASE_URL}assets/uploads/invoice/"
    const val PDF_QUOTATION_URL = "${BASE_URL}assets/uploads/estimation/"
    const val PDF_INSPECTION_URL = "${BASE_URL}assets/uploads/inspection/"
    const val DOCUMENT_URL = "${BASE_URL}assets/uploads/document/"
    const val CMS_URL = "${API_URL}service/getPage?PageName="

    const val OVERTIME = "overtime"
    const val LATEFINE = "latefine"
    const val PAGE_SIZE = 10

    const val TITLE = "title"

    const val TEN_MILISEC = 600000

    const val TEXT = "text"

    // Common Params
    const val METHOD = "method"
    const val BODY = "body"
    const val MESSAGE = "message"
    const val ERROR = "error"
    const val ROW_COUNT = "rowCount"
    const val DATA = "data"
    const val DATA1 = "data1"
    const val DATA_LEAD = "data_lead"
    const val DATA_SITE = "data_site"


    // ---Server Date Time--//
    const val DATE_FORMAT = "yyyy-MM-dd"


    const val MOBILE = "mobile"
    const val SERVICE_ID = "serviceId"
    const val UNAUTHORIZED = "unauthorized"


    //----- Lead Type-----
    const val HOT = "Hot"
    const val WARM = "Warm"
    const val COLD = "Cold"
    const val SILENT = "Silent"


    //--- Method Name-----

    const val METHOD_LOGIN = "checkLogin"
    const val METHOD_COLLECTION_LIST = "checkLogin"

    //------Bucket Size----
    const val oneTO30 = "1to30"
    const val threoneTO60 = "31to60"
    const val sixoneTO90 = "61to90"
    const val nineoneTO180 = "91to180"
    const val oneeightaboveTO180 = "180+"

}
package com.lightmicrofinance.app.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.databinding.ActivitySearchBinding
import com.lightmicrofinance.app.extention.*
import com.lightmicrofinance.app.fragment.*
import com.lightmicrofinance.app.modal.CenternameDataItem
import com.lightmicrofinance.app.modal.CenternameListModal
import com.lightmicrofinance.app.modal.UserStatusModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import com.lightmicrofinance.app.utils.Logger
import com.lightmicrofinance.app.utils.TimeStamp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SearchActivty : BaseActivity() {
    lateinit var binding: ActivitySearchBinding
    val myCalendar1 = Calendar.getInstance()
    val myCalendar2 = Calendar.getInstance()
    var centerNameList: ArrayList<String> = ArrayList()
    var adaptercenterName: ArrayAdapter<String>? = null
    var centerNameListArray: ArrayList<CenternameDataItem> = ArrayList()
    var itemCenterNameType: List<SearchableItem>? = null
    var centerName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.includes.txtTitle.text = getString(R.string.search)
        binding.includes.imgBack.setOnClickListener { finish() }

        binding.edtStartDate.setText(TimeStamp.getSpesificStartDateRange())
        binding.edtEndDate.setText(getYesterdayDate())

        if (intent.getStringExtra(Constant.DATA)!!.equals(Constant.COLLECTION_SUMMARY))
            binding.edtStartDate.setText(TimeStamp.getStartDateRange())
        else
            binding.edtStartDate.setText(TimeStamp.getSpesificStartDateRange())

        if (intent.getStringExtra(Constant.DATA)!!
                .equals(Constant.BUSINESS) || intent.getStringExtra(Constant.DATA)!!
                .equals(Constant.BUSINESS_SUMMARY) || intent.getStringExtra(Constant.DATA)!!
                .equals(Constant.PAR_SUMMARY) || intent.getStringExtra(Constant.DATA)!!
                .equals(Constant.COLLECTION_SUMMARY)
        ) {
            binding.inStartDate.visible()
            binding.inEndDate.visible()
            binding.linlayCenterName.invisible()
            binding.inCleintID.invisible()
            binding.inCleintName.invisible()
            binding.inLoanID.invisible()
        } else if (intent.getStringExtra(Constant.DATA)!!.equals(Constant.COLLECTION)) {
            binding.linlayCenterName.invisible()
        }

        binding.btnSearch.setOnClickListener { SearchData() }

        getCenterNameList()
        centerNameSpinnerListner()
        centerNameViewClick()

        val date: DatePickerDialog.OnDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker?, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                myCalendar1.set(Calendar.YEAR, year)
                myCalendar1.set(Calendar.MONTH, monthOfYear)
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd-MM-yyyy" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.edtStartDate.setText(sdf.format(myCalendar1.time))
            }
        }

        val date2: DatePickerDialog.OnDateSetListener =
            object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(
                    view: DatePicker?, year: Int, monthOfYear: Int,
                    dayOfMonth: Int
                ) {
                    myCalendar2.set(Calendar.YEAR, year)
                    myCalendar2.set(Calendar.MONTH, monthOfYear)
                    myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    val myFormat = "dd-MM-yyyy" //In which you need put here
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    binding.edtEndDate.setText(sdf.format(myCalendar2.time))
                }
            }




        binding.edtStartDate.setOnClickListener {
            val dialog = DatePickerDialog(
                this@SearchActivty,
                date,
                myCalendar1[Calendar.YEAR],
                myCalendar1[Calendar.MONTH],
                myCalendar1[Calendar.DAY_OF_MONTH]
            )
            dialog.getDatePicker().setMaxDate(Calendar.getInstance().timeInMillis - 86400000L)
            dialog.show()
            //  showPastDateTimePicker(this@SearchActivty, binding.edtStartDate)
        }


        binding.edtEndDate.setOnClickListener {
            /*  showPastDateTimePicker(
                  this@SearchActivty,
                  binding.edtEndDate
              )*/
            val dialog = DatePickerDialog(
                this@SearchActivty,
                date2,
                myCalendar2[Calendar.YEAR],
                myCalendar2[Calendar.MONTH],
                myCalendar2[Calendar.DAY_OF_MONTH]
            )

            val f = SimpleDateFormat("dd-MM-yyyy")
            val d = f.parse(binding.edtStartDate.getValue())
            dialog.getDatePicker().setMinDate(d.time)
            dialog.getDatePicker().setMaxDate(Calendar.getInstance().timeInMillis - 86400000L)
            dialog.show()
        }

        binding.edtStartDate.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


    }


    fun SearchData() {
        if (intent.getStringExtra(Constant.DATA)!!.equals(Constant.PAR)) {
            ParFragment.CenterName = centerName
            ParFragment.ClientID = binding.edtCleintID.getValue()
            ParFragment.CenterName = centerName
            ParFragment.LoanID = binding.edtLoanID.getValue()
        } else if (intent.getStringExtra(Constant.DATA)!!.equals(Constant.COLLECTION)) {
            CollectionFragment.ClientName = binding.edtCleintName.getValue()
            CollectionFragment.ClientID = binding.edtCleintID.getValue()
            CollectionFragment.LoanID = binding.edtLoanID.getValue()
            CollectionFragment.CenterName = centerName
        } else if (intent.getStringExtra(Constant.DATA)!!.equals(Constant.BUSINESS)) {
            BusinessFragment.StartDate = binding.edtStartDate.getValue()
            BusinessFragment.EndDate = binding.edtEndDate.getValue()
        } else if (intent.getStringExtra(Constant.DATA)!!.equals(Constant.BUSINESS_SUMMARY)) {
            BusinessSummaryFragment.StartDate = binding.edtStartDate.getValue()
            BusinessSummaryFragment.EndDate = binding.edtEndDate.getValue()
        } else if (intent.getStringExtra(Constant.DATA)!!.equals(Constant.COLLECTION_SUMMARY)) {
            CollectionSummaryFragment.StartDate = binding.edtStartDate.getValue()
            CollectionSummaryFragment.EndDate = binding.edtEndDate.getValue()
        }


        finish()
        // CenterName = binding.

    }

    private fun centerNameViewClick() {

        binding.view2.setOnClickListener {
            SearchableDialog(this@SearchActivty,
                itemCenterNameType!!,
                getString(R.string.center_name), { item, _ ->
                    binding.spCenterName.setSelection(item.id.toInt())
                }).show()
        }

    }

    private fun centerNameSpinnerListner() {
        binding.spCenterName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && centerNameListArray!!.size > position) {
                    if (position == 0) {
                        centerName = ""
                    } else {
                        centerName = centerNameListArray!!.get(position - 1).centerName.toString()
                    }
                    Logger.d("userIDq", centerName)

                }

            }
        }
    }

    fun getCenterNameList() {
        val params = HashMap<String, Any>()
        params[""] = ""
        Networking
            .with(this@SearchActivty)
            .getServices()
            .getCenterName(Networking.wrapParams(params))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CenternameListModal>() {
                override fun onSuccess(response: CenternameListModal) {
                    centerNameListArray!!.addAll(response.data)

                    var myList: MutableList<SearchableItem> = mutableListOf()
                    centerNameList!!.add(getString(R.string.center_name))

                    myList.add(SearchableItem(0, getString(R.string.center_name)))

                    for (items in response.data.indices) {
                        centerNameList!!.add(response.data.get(items).centerName.toString())
                        myList.add(
                            SearchableItem(
                                items.toLong() + 1,
                                centerNameList!!.get(items + 1)
                            )
                        )
                    }
                    itemCenterNameType = myList

                    adaptercenterName = ArrayAdapter(
                        this@SearchActivty,
                        R.layout.custom_spinner_item,
                        centerNameList!!
                    )
                    binding.spCenterName.setAdapter(adaptercenterName)


                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun checkUserSatus() {
        val params = HashMap<String, Any>()
        params["FECode"] = session.user.data?.fECode.toString()
        params["BMCode"] = session.user.data?.bMCode.toString()

        Networking
            .with(this)
            .getServices()
            .checkUserStatus(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<UserStatusModal>() {
                override fun onSuccess(response: UserStatusModal) {
                    val data = response.data
                    if (response.error == false) {
                        if (data != null) {
                            if (data.status == "0") {
                                session.isLoggedIn = false
                                goToActivityAndClearTask<LoginActivity>()
                            }
                        } else {
                            showAlert(response.message.toString())
                        }
                    } else {
                        showAlert(response.message.toString())
                    }

                }

                override fun onFailed(code: Int, message: String) {
                    showAlert(message)
                }

            }).addTo(autoDisposable)
    }

    override fun onResume() {
        super.onResume()
        checkUserSatus()
    }
}
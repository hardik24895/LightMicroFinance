package com.lightmicrofinance.commonproject.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import com.commonProject.extention.*
import com.commonProject.network.CallbackObserver
import com.commonProject.network.Networking
import com.commonProject.network.addTo
import com.commonProject.utils.Constant
import com.commonProject.utils.Logger
import com.commonProject.utils.TimeStamp
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.databinding.ActivitySearchBinding
import com.lightmicrofinance.commonproject.fragment.*
import com.lightmicrofinance.commonproject.modal.CenternameDataItem
import com.lightmicrofinance.commonproject.modal.CenternameListModal
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

        binding.edtStartDate.setText(TimeStamp.getSpesificStartDateRange())
        binding.edtEndDate.setText(getYesterdayDate())


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

        binding.includes.imgBack.setOnClickListener { finish() }

        if (intent.getStringExtra(Constant.DATA)!!
                .equals(Constant.BUSINESS) || intent.getStringExtra(Constant.DATA)!!
                .equals(Constant.BUSINESS_SUMMARY) || intent.getStringExtra(Constant.DATA)!!
                .equals(Constant.PAR_SUMMARY)
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
            BusinessSummaryFragment.StartDate =
                binding.edtStartDate.getValue()
            BusinessSummaryFragment.EndDate = binding.edtEndDate.getValue()
        } else if (intent.getStringExtra(Constant.DATA)!!.equals(Constant.PAR_SUMMARY)) {
            ParSummaryFragment.StartDate = binding.edtStartDate.getValue()
            ParSummaryFragment.EndDate = binding.edtEndDate.getValue()
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
}
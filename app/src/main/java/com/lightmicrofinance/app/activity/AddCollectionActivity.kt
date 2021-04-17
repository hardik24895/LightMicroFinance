package com.lightmicrofinance.app.activity


import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.databinding.ActivityAddCollectionBinding
import com.lightmicrofinance.app.extention.*
import com.lightmicrofinance.app.modal.*
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import com.lightmicrofinance.app.utils.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


class AddCollectionActivity : BaseActivity() {
    lateinit var binding: ActivityAddCollectionBinding

    var collectionDataItem: CollectionDataItem? = null
    val myCalendar1 = Calendar.getInstance()
    val myCalendar2 = Calendar.getInstance()

    var reasonNameList: ArrayList<String> = ArrayList()
    var adapterreasonName: ArrayAdapter<String>? = null
    var reasonNameListArray: ArrayList<ReasonDataItem> = ArrayList()
    var iteamReasonNameType: List<SearchableItem>? = null
    var reasonId: String = "-1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCollectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.includes.txtTitle.text = getString(R.string.collection)
        binding.includes.imgBack.setOnClickListener { finish() }

        collectionDataItem = intent.getSerializableExtra(Constant.DATA) as CollectionDataItem
        setData()
        binding.edDate.setText(getCurrentDate())
        dateDisplay()

        getReasonList()
        reasonNameSpinnerListner()
        reasonNameViewClick()
        binding.edtPayment.setText(collectionDataItem?.currentDemand)
        setCalculation()
        poromiseDateDisplay()


        binding.edtPayment.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                setCalculation()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.btnSubmit.setOnClickListener {
            validation()
        }

    }

    fun dateDisplay() {
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
                binding.edDate.setText(sdf.format(myCalendar1.time))
            }
        }

        binding.edDate.setOnClickListener {
            val dialog = DatePickerDialog(
                this@AddCollectionActivity,
                date,
                myCalendar1[Calendar.YEAR],
                myCalendar1[Calendar.MONTH],
                myCalendar1[Calendar.DAY_OF_MONTH]
            )
            dialog.getDatePicker().setMaxDate(Calendar.getInstance().timeInMillis)
            dialog.show()
        }
    }

    fun poromiseDateDisplay() {
        val date: DatePickerDialog.OnDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(
                view: DatePicker?, year: Int, monthOfYear: Int,
                dayOfMonth: Int
            ) {
                myCalendar2.set(Calendar.YEAR, year)
                myCalendar2.set(Calendar.MONTH, monthOfYear)
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd-MM-yyyy" //In which you need put here
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                binding.edPromiseDate.setText(sdf.format(myCalendar2.time))
            }
        }

        binding.edPromiseDate.setOnClickListener {
            val dialog = DatePickerDialog(
                this@AddCollectionActivity,
                date,
                myCalendar2[Calendar.YEAR],
                myCalendar2[Calendar.MONTH],
                myCalendar2[Calendar.DAY_OF_MONTH]
            )
            dialog.getDatePicker().minDate = Calendar.getInstance().timeInMillis
            dialog.show()
        }
    }

    fun setCalculation() {
        try {
            if (!binding.edtPayment.isEmpty()) {

                val todayCOllection = binding.edtPayment.getValue().toFloat()
                val totalCollection =
                    todayCOllection + collectionDataItem?.originalCollection?.toFloat()!!
                val originalDemand = collectionDataItem?.originalDemand?.toFloat()!!
                val originalCOllection = collectionDataItem?.originalCollection?.toFloat()!!
                val currentDemand = collectionDataItem?.currentDemand?.toFloat()!!

                var regularCollection = 0f
                var advanceCOllection = 0f

                var df = DecimalFormat("##.##")

                if (totalCollection <= originalDemand) {
                    regularCollection = totalCollection
                } else {
                    regularCollection = originalDemand
                }

                if (originalCOllection < originalDemand - currentDemand) {
                    regularCollection =
                        regularCollection + originalDemand - currentDemand - originalCOllection
                }

                if (totalCollection - regularCollection < 0) {
                    advanceCOllection = 0f
                } else {
                    advanceCOllection = totalCollection - regularCollection
                }


                val pending = originalDemand - regularCollection
                val percentage = regularCollection * 100 / originalDemand



                binding.txtTotalCollection.text =
                    getString(R.string.RS) + " " + totalCollection.toString()
                binding.txtRegularLocation.text =
                    getString(R.string.RS) + " " + regularCollection.toString()

                binding.txtAdvanceCollection.text =
                    getString(R.string.RS) + " " + advanceCOllection.toString()
                binding.txtPending.text = getString(R.string.RS) + " " + pending.toString()
                binding.txtPercentage.text = df.format(percentage).toString() + "%"


            } else {
                binding.txtTotalCollection.text = ""
                binding.txtRegularLocation.text = ""
                binding.txtAdvanceCollection.text = ""
                binding.txtPending.text = ""
                binding.txtPercentage.text = ""
            }
        } catch (e: Exception) {

        }


    }

    fun setData() {

        if (intent.getStringExtra(Constant.TYPE) == Constant.PENDING) {
            binding.imgCardBg.setImageResource(R.drawable.orange_card)
        } else if (intent.getStringExtra(Constant.TYPE) == Constant.PARTIALY) {
            binding.imgCardBg.setImageResource(R.drawable.blue_card)
        } else {
            binding.imgCardBg.setImageResource(R.drawable.green_card)
        }

        binding.txtCleintId.text = collectionDataItem?.clientID
        binding.txtLoanID.text = collectionDataItem?.loanID
        binding.txtCenterName.text = collectionDataItem?.centerName
        binding.txtBranchName.text = collectionDataItem?.branch
        binding.txtDate.text = collectionDataItem?.dueDate
        binding.txtClientName.text = collectionDataItem?.clientName
        binding.txtAmount.text = getString(R.string.RS) + " " + collectionDataItem?.currentDemand
        binding.txtOriginalCollection.text =
            getString(R.string.RS) + " " + collectionDataItem?.originalCollection
        binding.txtOriginalDemand.text =
            getString(R.string.RS) + " " + collectionDataItem?.originalDemand
        binding.txtRegularCollection.text =
            getString(R.string.RS) + " " + collectionDataItem?.regularCollection
        binding.txtTotalCollections.text =
            getString(R.string.RS) + " " + collectionDataItem?.totalCollection
        binding.txtPendings.text = getString(R.string.RS) + " " + collectionDataItem?.pending
        binding.txtPrec.text = collectionDataItem?.percentage


        binding.rg.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            val rb = findViewById<View>(checkedId) as RadioButton
            if (rb?.text.toString() == getString(R.string.full)) {
                binding.linlayReason.invisible()
                binding.inPayment.visible()
                binding.edtPayment.setText(collectionDataItem?.currentDemand)
                setCalculation()
            } else if (rb.text.toString() == getString(R.string.partial)) {
                binding.edtPayment.setText("0")
                binding.inPayment.visible()
                binding.linlayReason.invisible()
                setCalculation()
            } else {
                binding.edtPayment.setText("0")
                binding.inPayment.invisible()
                binding.linlayReason.visible()
            }

        })

    }

    fun removeLastChar(s: String): String {
        return if (s.length == 0) {
            s
        } else s.substring(0, s.length - 1)
    }

    fun validation() {
        when {
            binding.edtPayment.isEmpty() -> {
                binding.mainview.showSnackBar("Enter Amount")
            }

            binding.linlayReason.isVisible && reasonId == "-1" -> {
                binding.mainview.showSnackBar("Select Reason")
            }

            binding.inPromiseDate.isVisible && binding.edPromiseDate.isEmpty() -> {
                binding.mainview.showSnackBar("Enter Promise Date")
            }

            else -> {

                addCollection()

                //  Logger.d("reason", reasonNameListArray.get(binding.spReasonName.selectedItemPosition - 1).reason.toString())
               /* Logger.d("percentage", removeLastChar(binding.txtPercentage.text.toString()))
                Logger.d("pending", binding.txtPending.text.toString().substring(2))
                Logger.d("regular", binding.txtRegularLocation.text.toString().substring(2))
                Logger.d("totalCollection", binding.txtTotalCollection.text.toString().substring(2))
                Logger.d("advance", binding.txtAdvanceCollection.text.toString().substring(2))*/


            }


        }

    }

    private fun reasonNameViewClick() {

        binding.view2.setOnClickListener {
            SearchableDialog(this@AddCollectionActivity,
                iteamReasonNameType!!,
                getString(R.string.center_name), { item, _ ->
                    binding.spReasonName.setSelection(item.id.toInt())
                }).show()
        }

    }

    private fun reasonNameSpinnerListner() {
        binding.spReasonName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && reasonNameListArray.size > position - 1) {
                    if (position == 0) {
                        reasonId = "-1"
                        binding.inPromiseDate.invisible()
                    } else {
                        reasonId = reasonNameListArray.get(position - 1).reasonID.toString()
                        if (reasonNameListArray.get(position - 1).isPOT == "1")
                            binding.inPromiseDate.visible()
                        else
                            binding.inPromiseDate.invisible()

                    }
                    Logger.d("userIDq", reasonId)

                }

            }
        }
    }

    fun getReasonList() {
        val params = HashMap<String, Any>()
        params[""] = ""
        Networking
            .with(this@AddCollectionActivity)
            .getServices()
            .getReason(Networking.wrapParams(params))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<ReasonListModal>() {
                override fun onSuccess(response: ReasonListModal) {
                    reasonNameListArray!!.addAll(response.data)

                    var myList: MutableList<SearchableItem> = mutableListOf()
                    reasonNameList!!.add(getString(R.string.reason))

                    myList.add(SearchableItem(0, getString(R.string.reason)))

                    for (items in response.data.indices) {
                        reasonNameList!!.add(response.data.get(items).reason.toString())
                        myList.add(
                            SearchableItem(
                                items.toLong() + 1,
                                reasonNameList!!.get(items + 1)
                            )
                        )
                    }
                    iteamReasonNameType = myList

                    adapterreasonName = ArrayAdapter(
                        this@AddCollectionActivity,
                        R.layout.custom_spinner_item,
                        reasonNameList!!
                    )
                    binding.spReasonName.setAdapter(adapterreasonName)


                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun addCollection() {
        val selectedId: Int = binding.rg.getCheckedRadioButtonId()
        val rb = findViewById<View>(selectedId) as? RadioButton

        showProgressbar()
        val params = HashMap<String, Any>()
        params["TotalCollection"] = binding.txtTotalCollection.text.toString().substring(2)
        params["TodaysCollection"] = binding.edtPayment.getValue()
        params["RegularCollection"] = binding.txtRegularLocation.text.toString().substring(2)
        params["AdvanceCollection"] = binding.txtAdvanceCollection.text.toString().substring(2)
        params["Pending"] = binding.txtPending.text.toString().substring(2)
        params["Percentage"] = removeLastChar(binding.txtPercentage.text.toString())
        params["Remark"] = binding.edtRemarks.getValue()
        if (reasonId != "-1") {
            params["Reason"] =
                reasonNameListArray.get(binding.spReasonName.selectedItemPosition - 1).reason.toString()
        } else {
            params["Reason"] = ""
        }

        if (binding.edPromiseDate.isEmpty()) {
            params["PromiseDate"] = ""
        } else {
            params["PromiseDate"] = binding.edPromiseDate.getValue()
        }




        params["PaymentReceivedType"] = rb?.text.toString()
        params["PaymentDate"] = binding.edDate.getValue()
        params["CollectionID"] = collectionDataItem?.collectionID.toString()


        Networking
            .with(this)
            .getServices()
            .addCollection(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommanIDModal>() {
                override fun onSuccess(response: CommanIDModal) {
                    val data = response.data
                    hideProgressbar()
                    if (response.error == false) {
                        if (data != null) {
                            finish()
                        } else {
                            showAlert(response.message.toString())
                        }
                    } else {
                        showAlert(response.message.toString())
                    }

                }

                override fun onFailed(code: Int, message: String) {
                    showAlert(message)
                    hideProgressbar()
                }

            }).addTo(autoDisposable)
    }

    override fun onResume() {
        super.onResume()
        checkUserSatus()
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
}
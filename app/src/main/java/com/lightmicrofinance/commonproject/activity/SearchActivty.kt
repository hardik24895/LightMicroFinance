package com.lightmicrofinance.commonproject.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.commonProject.extention.getValue
import com.commonProject.extention.showAlert
import com.commonProject.network.CallbackObserver
import com.commonProject.network.Networking
import com.commonProject.network.addTo
import com.commonProject.utils.Constant
import com.commonProject.utils.Logger
import com.lightmicrofinance.commonproject.R

import com.lightmicrofinance.commonproject.databinding.ActivitySearchBinding
import com.lightmicrofinance.commonproject.fragment.CollectionFragment.Companion.CenterName
import com.lightmicrofinance.commonproject.fragment.CollectionFragment.Companion.ClientID
import com.lightmicrofinance.commonproject.fragment.CollectionFragment.Companion.ClientName
import com.lightmicrofinance.commonproject.fragment.CollectionFragment.Companion.LoanID
import com.lightmicrofinance.commonproject.modal.CenternameDataItem
import com.lightmicrofinance.commonproject.modal.CenternameListModal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem

class SearchActivty : BaseActivity() {
    lateinit var binding: ActivitySearchBinding

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

        /* if (intent.getStringExtra(Constant.DATA)!!.equals(Constant.COLLECTION)) {

         }*/

        binding.btnSearch.setOnClickListener { SearchData() }

        getCenterNameList()
        centerNameSpinnerListner()
        centerNameViewClick()

    }

    fun SearchData() {
        ClientName = binding.edtCleintName.getValue()
        ClientID = binding.edtCleintID.getValue()
        LoanID = binding.edtLoanID.getValue()
        CenterName = centerName
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
        params[""] =  ""
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
                    for (items in response.data.indices) {
                        centerNameList!!.add(response.data.get(items).centerName.toString())
                        myList.add(SearchableItem(items.toLong(), centerNameList!!.get(items)))
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
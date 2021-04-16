package com.lightmicrofinance.app.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.activity.LoginActivity
import com.lightmicrofinance.app.activity.SearchActivty
import com.lightmicrofinance.app.databinding.FragamentCollectionSummaryBinding
import com.lightmicrofinance.app.extention.getYesterdayDate
import com.lightmicrofinance.app.extention.invisible
import com.lightmicrofinance.app.extention.goToActivityAndClearTask
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.extention.visible
import com.lightmicrofinance.app.modal.CollectionSummaryReportModal
import com.lightmicrofinance.app.modal.FEDataItem
import com.lightmicrofinance.app.modal.FEDateModel
import com.lightmicrofinance.app.modal.UserStatusModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import com.lightmicrofinance.app.utils.TimeStamp
import com.lightmicrofinance.app.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem


class CollectionSummaryFragment : BaseFragment() {

    private var _binding: FragamentCollectionSummaryBinding? = null

    private val binding get() = _binding!!

    companion object {
        var StartDate: String = TimeStamp.getStartDateRange()
        var EndDate: String = getYesterdayDate()

    }

    var selectedFEId: String = ""
    var FENameList: ArrayList<String> = ArrayList()
    var adapterFE: ArrayAdapter<String>? = null
    var FEListArray: ArrayList<FEDataItem> = ArrayList()
    var itemFEType: List<SearchableItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragamentCollectionSummaryBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Utils.checkUserIsBM(session.user.data?.userType!!)) {
            _binding?.linlayFEList?.visible()
        } else {
            _binding?.linlayFEList?.invisible()
        }


        FEViewClick()
        FESpinnerListner()

    }

    override fun onResume() {
        super.onResume()
        getFEList()
        binding.txtSelectedDate.text = StartDate + " To " + EndDate
        checkUserSatus()
        getSummaryData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            /* R.id.action_add -> {
                 if (checkUserRole(
                         session.roleData.data.visitor.isEdit.toString(),
                         requireContext()
                     )
                 )
                     showDialog()
                 return true
             }*/
            R.id.action_filter -> {
                val intent = Intent(context, SearchActivty::class.java)
                intent.putExtra(Constant.DATA, Constant.COLLECTION_SUMMARY)
                startActivity(intent)
                Animatoo.animateCard(context)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun getSummaryData() {
        showProgressbar()
        val params = HashMap<String, Any>()
        if (Utils.checkUserIsBM(session.user.data?.userType!!)) {
            params["FECode"] = selectedFEId
        } else {
            params["FECode"] = session.user.data?.fECode.toString()
        }
        params["BMCode"] = session.user.data?.bMCode.toString()
        params["StartDate"] = StartDate
        params["EndDate"] = EndDate

        Log.d("Request::::>", "getSummaryData: " + params)

        Networking
            .with(requireContext())
            .getServices()
            .getCollectionSummaryReport(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CollectionSummaryReportModal>() {
                override fun onSuccess(response: CollectionSummaryReportModal) {
                    val data = response.data
                    hideProgressbar()
                    if (response.error == false) {

                        binding.txtCleintsCollected.text = data?.client?.collected
                        binding.txtCleintsPartialy.text = data?.client?.partialy
                        binding.txtCleintsPending.text = data?.client?.pending
                        binding.txtCleintsGtotal.text = data?.client?.total

                        binding.txtDemandCollected.text = data?.demand?.collected
                        binding.txtDemandPartialy.text = data?.demand?.partialy
                        binding.txtDemandPending.text = data?.demand?.pending
                        binding.txtDemandGtotal.text = data?.demand?.total


                        binding.txtCollectionCollected.text = data?.collection?.collected
                        binding.txtCollectionPartialy.text = data?.collection?.partialy
                        binding.txtCollectionPending.text = data?.collection?.pending
                        binding.txtCollectionGtotal.text = data?.collection?.total


                        binding.txtPendingpCollected.text = data?.pending?.collected
                        binding.txtPendingpPartialy.text = data?.pending?.partialy
                        binding.txtPendingpPending.text = data?.pending?.pending
                        binding.txtPendingpGtotal.text = data?.pending?.total


                        binding.txtParcentageCollected.text = data?.percentage?.collected
                        binding.txtParcentagePartialy.text = data?.percentage?.partialy
                        binding.txtParcentagePending.text = data?.percentage?.pending
                        binding.txtParcentageGtotal.text = data?.percentage?.total


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

    fun checkUserSatus() {
        val params = java.util.HashMap<String, Any>()
        params["FECode"] = session.user.data?.fECode.toString()
        params["BMCode"] = session.user.data?.bMCode.toString()

        Networking
            .with(requireContext())
            .getServices()
            .checkUserStatus(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<UserStatusModal>() {
                override fun onSuccess(response: UserStatusModal) {
                    val data = response.data
                    if (response.error == false) {
                        if (data != null) {
                            if (data.status == "0")
                                goToActivityAndClearTask<LoginActivity>()
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

    fun getFEList() {
        FEListArray.clear()
        FENameList.clear()
        val params = HashMap<String, Any>()
        params.put("PageSize", "-1")
        params.put("CurrentPage", "1")
        params.put("BMCode", session.user.data?.bMCode.toString())
        params.put("Status", "-1")

        Networking
            .with(requireContext())
            .getServices()
            .getFEList(Networking.wrapParams(params))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<FEDateModel>() {
                override fun onSuccess(response: FEDateModel) {
                    FEListArray.addAll(response.data)

                    var myList: MutableList<SearchableItem> = mutableListOf()
                    FENameList.add(getString(R.string.select_field_executive))


                    myList.add(SearchableItem(0, getString(R.string.select_field_executive)))
                    for (items in response.data.indices) {
                        FENameList.add(response.data.get(items).name.toString())
                        myList.add(
                            SearchableItem(
                                items.toLong() + 1,
                                FENameList.get(items + 1)
                            )
                        )
                    }
                    itemFEType = myList

                    adapterFE = ArrayAdapter(
                        requireContext(),
                        R.layout.custom_spinner_item,
                        FENameList
                    )
                    _binding?.spFEList?.setAdapter(adapterFE)


                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    private fun FEViewClick() {
        _binding?.viewFE?.setOnClickListener {
            itemFEType?.let { it1 ->
                SearchableDialog(requireContext(),
                    it1,
                    getString(R.string.select_field_executive), { item, _ ->
                        _binding?.spFEList?.setSelection(item.id.toInt())
                    }).show()
            }

        }


    }

    private fun FESpinnerListner() {
        _binding?.spFEList?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && FEListArray.size > position-1) {
                    if (position == 0) {
                        //    CenterName = ""
                        // spinnerAPICall2()
                        // CenterName = FEListArray.get(position - 1).name.toString()
                        //   spinnerAPICall()
                        selectedFEId = ""

                    }else{
                        selectedFEId = FEListArray.get(position - 1).fECode.toString()
                    }


                    getSummaryData()
                }

            }
        }
    }
}
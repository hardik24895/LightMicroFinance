package com.lightmicrofinance.app.fragment


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.activity.LoginActivity
import com.lightmicrofinance.app.activity.SearchActivty
import com.lightmicrofinance.app.databinding.FragementSummaryBusinessBinding
import com.lightmicrofinance.app.extention.getYesterdayDate
import com.lightmicrofinance.app.extention.invisible
import com.lightmicrofinance.app.extention.goToActivityAndClearTask
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.extention.visible
import com.lightmicrofinance.app.modal.BusinessSummaryModal
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


class BusinessSummaryFragment : BaseFragment() {

    private var _binding: FragementSummaryBusinessBinding? = null

    private val binding get() = _binding!!

    companion object {
        var StartDate: String = TimeStamp.getSpesificStartDateRange()
        var EndDate: String = getYesterdayDate()

    }

    var FENameList: ArrayList<String> = ArrayList()
    var adapterFE: ArrayAdapter<String>? = null
    var FEListArray: ArrayList<FEDataItem> = ArrayList()
    var itemFEType: List<SearchableItem>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragementSummaryBusinessBinding.inflate(inflater, container, false)
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
        getFEList()
        FEViewClick()
        FESpinnerListner()

    }

    override fun onResume() {
        super.onResume()
        checkUserSatus()
        binding.txtSelectedDate.text = StartDate + " To " + EndDate
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
                intent.putExtra(Constant.DATA, Constant.BUSINESS_SUMMARY)
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
        params["FECode"] = session.user.data?.fECode.toString()
        params["BMCode"] = session.user.data?.bMCode.toString()
        params["StartDate"] = StartDate
        params["EndDate"] = EndDate

        Networking
            .with(requireContext())
            .getServices()
            .getBusinessSammary(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<BusinessSummaryModal>() {
                override fun onSuccess(response: BusinessSummaryModal) {
                    val data = response.data
                    hideProgressbar()
                    if (response.error == false) {
                        binding.txtLENewPlan.text = data?.lENew?.lENew
                        binding.txtLENewActual.text = data?.lENew?.achLENew
                        binding.txtLENewDifferent.text = data?.lENew?.diff
                        binding.txtLENewDifferent.setTextColor(Color.parseColor(data?.lENew?.color.toString()))
                        binding.txtLENewAchived.setTextColor(Color.parseColor(data?.lENew?.color.toString()))
                        binding.txtLENewAchived.text = data?.lENew?.percentage

                        binding.txtLEReNewPlan.text = data?.lERenew?.lERenew
                        binding.txtLEReNewActual.text = data?.lERenew?.achLERenew
                        binding.txtLEReNewDifferent.text = data?.lERenew?.diff
                        binding.txtLEReNewDifferent.setTextColor(Color.parseColor(data?.lERenew?.color.toString()))
                        binding.txtLEReNewAchived.setTextColor(Color.parseColor(data?.lERenew?.color.toString()))
                        binding.txtLEReNewAchived.text = data?.lERenew?.percentage

                        binding.txtTotalLEPlan.text = data?.totalLE?.totalLE
                        binding.txtTotalLEActual.text = data?.totalLE?.achTotalLE
                        binding.txtTotalLEDifferent.text = data?.totalLE?.diff
                        binding.txtTotalLEDifferent.setTextColor(Color.parseColor(data?.totalLE?.color.toString()))
                        binding.txtTotalLEAchived.setTextColor(Color.parseColor(data?.totalLE?.color.toString()))
                        binding.txtTotalLEAchived.text = data?.lENew?.percentage

                        binding.txtDDPlan.text = data?.dDDone?.dDDone
                        binding.txtDDActual.text = data?.dDDone?.achDDDone
                        binding.txtDDDifferent.text = data?.dDDone?.diff
                        binding.txtDDDifferent.setTextColor(Color.parseColor(data?.dDDone?.color.toString()))
                        binding.txtDDAchived.setTextColor(Color.parseColor(data?.dDDone?.color.toString()))
                        binding.txtDDAchived.text = data?.dDDone?.percentage

                        binding.txtDDvePlan.text = data?.dDPositive?.dDPositive
                        binding.txtDDveActual.text = data?.dDPositive?.achDDPositive
                        binding.txtDDveDifferent.text = data?.dDPositive?.diff
                        binding.txtDDveDifferent.setTextColor(Color.parseColor(data?.dDPositive?.color.toString()))
                        binding.txtDDveAchived.setTextColor(Color.parseColor(data?.dDPositive?.color.toString()))
                        binding.txtDDveAchived.text = data?.dDPositive?.percentage

                        binding.txtGRTPlan.text = data?.gRT?.gRT
                        binding.txtGRTActual.text = data?.gRT?.achGRT
                        binding.txtGRTDifferent.text = data?.gRT?.diff
                        binding.txtGRTDifferent.setTextColor(Color.parseColor(data?.gRT?.color.toString()))
                        binding.txtGRTAchived.setTextColor(Color.parseColor(data?.gRT?.color.toString()))
                        binding.txtGRTAchived.text = data?.gRT?.percentage

                        binding.textDistClientPlan.text = data?.disbClient?.disbClient
                        binding.textDistClientActual.text = data?.disbClient?.achDisbClient
                        binding.textDistClientDifferent.text = data?.disbClient?.diff
                        binding.textDistClientDifferent.setTextColor(Color.parseColor(data?.disbClient?.color.toString()))
                        binding.textDistClientAchived.setTextColor(Color.parseColor(data?.disbClient?.color.toString()))
                        binding.textDistClientAchived.text = data?.disbClient?.percentage

                        binding.textDistAmountPlan.text = data?.disbAmount?.disbAmount
                        binding.textDistAmountActual.text = data?.disbAmount?.achDisbAmount
                        binding.textDistAmountDifferent.text = data?.disbAmount?.diff
                        binding.textDistAmountDifferent.setTextColor(Color.parseColor(data?.disbAmount?.color.toString()))
                        binding.textDistAmountAchived.setTextColor(Color.parseColor(data?.disbAmount?.color.toString()))
                        binding.textDistAmountAchived.text = data?.disbAmount?.percentage

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
                if (position != -1 && FEListArray.size > position) {
                    if (position == 0) {
                        //    CenterName = ""
                        // spinnerAPICall2()

                    } else {
                        // CenterName = FEListArray.get(position - 1).name.toString()
                        //   spinnerAPICall()
                    }
                    // Logger.d("userIDq", CenterName)

                }

            }
        }
    }
}
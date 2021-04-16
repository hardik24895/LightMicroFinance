package com.lightmicrofinance.app.fragment


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.commonProject.interfaces.LoadMoreListener
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.activity.LoginActivity
import com.lightmicrofinance.app.adapter.GoalSheetAdapter
import com.lightmicrofinance.app.databinding.FragmentParBinding
import com.lightmicrofinance.app.extention.goToActivityAndClearTask
import com.lightmicrofinance.app.extention.invisible
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.extention.visible
import com.lightmicrofinance.app.modal.*
import com.lightmicrofinance.app.modal.BusinessListDataItem
import com.lightmicrofinance.app.modal.GoalsheetDataItem
import com.lightmicrofinance.app.modal.GoalsheetModal
import com.lightmicrofinance.app.modal.UserStatusModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import com.lightmicrofinance.app.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem


class GoalSheetFragment : BaseFragment(), GoalSheetAdapter.OnItemSelected {

    private var _binding: FragmentParBinding? = null

    private val binding get() = _binding!!

    var adapter: GoalSheetAdapter? = null

    private val list: MutableList<GoalsheetDataItem> = mutableListOf()
    var status = Constant.PENDING
    var page: Int = 1
    var hasNextPage: Boolean = true

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
        _binding = FragmentParBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding?.recyclerView?.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !_binding?.recyclerView?.isLoading!!) {
                    _binding?.progressbar?.visible()
                    getBusinessList(++page)
                }
            }
        })

        _binding?.swipeRefreshLayout?.setOnRefreshListener {

            page = 1
            list.clear()
            hasNextPage = true
            _binding?.recyclerView?.isLoading = true
            adapter?.notifyDataSetChanged()
            getBusinessList(page)
        }

        if (Utils.checkUserIsBM(session.user.data?.userType!!)) {
            _binding?.linlayFEList?.visible()
        } else {
            _binding?.linlayFEList?.invisible()
        }


        FEViewClick()
        FESpinnerListner()
    }

    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(requireContext())
        _binding?.recyclerView?.layoutManager = layoutManager
        adapter = GoalSheetAdapter(requireContext(), list, session, status, this)
        _binding?.recyclerView?.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        checkUserSatus()
        getFEList()
        page = 1
        list.clear()
        hasNextPage = true
        _binding?.swipeRefreshLayout?.isRefreshing = true
        setupRecyclerView()
        _binding?.recyclerView?.isLoading = true
        getBusinessList(page)

    }


    fun getBusinessList(page: Int) {

        val params = HashMap<String, Any>()
        params["PageSize"] = Constant.PAGE_SIZE
        params["CurrentPage"] = page
        if (Utils.checkUserIsBM(session.user.data?.userType!!)) {
            params["FECode"] = selectedFEId
        } else {
            params["FECode"] = session.user.data?.fECode.toString()
        }
        params["BMCode"] = session.user.data?.bMCode.toString()
        params["StartDate"] = BusinessFragment.StartDate
        params["EndDate"] = BusinessFragment.EndDate

        Log.d("Request::::>", "getBusinessList: " + params)

        Networking
            .with(requireContext())
            .getServices()
            .getGoalSheet(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<GoalsheetModal>() {
                override fun onSuccess(response: GoalsheetModal) {
                    if (list.size > 0) {
                        _binding?.progressbar?.invisible()
                    }
                    _binding?.swipeRefreshLayout?.isRefreshing = false

                    if (response.error == false) {
                        list.addAll(response.data)
                        adapter?.notifyItemRangeInserted(
                            list.size.minus(response.data.size),
                            list.size
                        )
                        hasNextPage = list.size < response.rows
                    }

                    refreshData(getString(R.string.no_data_found), 1)
                }

                override fun onFailed(code: Int, message: String) {
                    if (list.size > 0) {
                        _binding?.progressbar?.invisible()
                    }
                    showAlert(getString(R.string.show_server_error))
                    refreshData(message, code)
                }

            }).addTo(autoDisposable)
    }


    private fun refreshData(msg: String?, code: Int) {
        _binding?.recyclerView?.setLoadedCompleted()
        _binding?.swipeRefreshLayout?.isRefreshing = false
        adapter?.notifyDataSetChanged()

        if (list.size > 0) {
            _binding?.imgNodata?.invisible()
            _binding?.recyclerView?.visible()
        } else {
            _binding?.imgNodata?.visible()
            if (code == 0)
                _binding?.imgNodata?.setImageResource(R.drawable.no_internet_bg)
            else
                _binding?.imgNodata?.setImageResource(R.drawable.nodata)
            _binding?.recyclerView?.invisible()
        }
    }

    override fun onItemSelect(position: Int, data: BusinessListDataItem, action: String) {

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
                    page = 1
                    list.clear()
                    hasNextPage = true
                    _binding?.swipeRefreshLayout?.isRefreshing = true
                    setupRecyclerView()
                    _binding?.recyclerView?.isLoading = true
                    getBusinessList(page)
                }

            }
        }
    }
}
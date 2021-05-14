package com.lightmicrofinance.app.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.commonProject.interfaces.LoadMoreListener
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.activity.AddCollectionActivity
import com.lightmicrofinance.app.activity.LoginActivity
import com.lightmicrofinance.app.activity.SearchActivty
import com.lightmicrofinance.app.adapter.CollectionAdapter
import com.lightmicrofinance.app.databinding.FragmentCollectionBinding
import com.lightmicrofinance.app.extention.goToActivityAndClearTask
import com.lightmicrofinance.app.extention.invisible
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.extention.visible
import com.lightmicrofinance.app.modal.*
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import com.lightmicrofinance.app.utils.Logger
import com.lightmicrofinance.app.utils.Utils.checkUserIsBM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem


class CollectionFragment : BaseFragment(), CollectionAdapter.OnItemSelected {

    var adapter: CollectionAdapter? = null
    var centerNameList: ArrayList<String> = ArrayList()
    var adaptercenterName: ArrayAdapter<String>? = null
    var centerNameListArray: ArrayList<CenternameDataItem> = ArrayList()
    var itemCenterNameType: List<SearchableItem>? = null
    private val list: MutableList<CollectionDataItem> = mutableListOf()
    var status = Constant.ALL
    var page: Int = 1
    var hasNextPage: Boolean = true


    var selectedFEId: String = ""
    var FENameList: ArrayList<String> = ArrayList()
    var adapterFE: ArrayAdapter<String>? = null
    var FEListArray: ArrayList<FEDataItem> = ArrayList()
    var itemFEType: List<SearchableItem>? = null


    companion object {
        var CenterName: String = ""
        var LoanID: String = ""
        var ClientID: String = ""
        var ClientName: String = ""
        var StartDate: String = ""
        var EndDate: String = ""
    }

    private lateinit var _binding: FragmentCollectionBinding

    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCenterNameList(Constant.ALL)
        centerNameSpinnerListner()
        centerNameViewClick()
        FESpinnerListner()
        FEViewClick()

        _binding.txtPending.setOnClickListener {
            if (!_binding.txtPending.isSelected) {
                _binding.txtPending.isSelected = true
                _binding.txtAll.isSelected = false
                _binding.txtCollected.isSelected = false
                _binding.txtVisitDone.isSelected = false
                status = Constant.PENDING
                getCenterNameList(Constant.PENDING)

            }

        }
        _binding.txtAll.setOnClickListener {
            if (!_binding.txtAll.isSelected) {
                _binding.txtPending.isSelected = false
                _binding.txtAll.isSelected = true
                _binding.txtCollected.isSelected = false
                _binding.txtVisitDone.isSelected = false
                status = Constant.ALL
                getCenterNameList(Constant.ALL)
                //getRefreshData()
            }


        }
        _binding.txtCollected.setOnClickListener {
            if (!_binding.txtCollected.isSelected) {
                _binding.txtPending.isSelected = false
                _binding.txtAll.isSelected = false
                _binding.txtCollected.isSelected = true
                _binding.txtVisitDone.isSelected = false
                status = Constant.COLLECTED
                getCenterNameList(Constant.COLLECTED)
                //getRefreshData()
            }

        }
        _binding.txtVisitDone.setOnClickListener {
            if (!_binding.txtVisitDone.isSelected) {
                _binding.txtPending.isSelected = false
                _binding.txtAll.isSelected = false
                _binding.txtCollected.isSelected = false
                _binding.txtVisitDone.isSelected = true
                status = Constant.PAYMENT
                getCenterNameList(Constant.PAYMENT)
                //getRefreshData()
            }

        }

        _binding.rvSwipe.recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !_binding.rvSwipe.recyclerView.isLoading) {
                    _binding.rvSwipe.progressbar.visible()
                    getCollectionList(++page)
                }
            }
        })

        _binding.rvSwipe.swipeRefreshLayout.setOnRefreshListener {
            CenterName = ""
            ClientID = ""
            LoanID = ""
            ClientName = ""
            StartDate = ""
            EndDate = ""
            page = 1
            list.clear()
            hasNextPage = true
            _binding.rvSwipe.recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            _binding.spCenterName.setSelection(0)
            getCollectionList(page)
        }



        if (checkUserIsBM(session.user.data?.userType!!)) {
            getFEList()
            _binding.linlayFEList.visible()
        } else {
            _binding.linlayFEList.invisible()
        }


    }

    fun getRefreshData() {
        CenterName = ""
        ClientID = ""
        LoanID = ""
        ClientName = ""
        StartDate = ""
        EndDate = ""
        page = 1
        list.clear()
        setupRecyclerView()
        hasNextPage = true
        _binding.rvSwipe.swipeRefreshLayout.isRefreshing = true
        _binding.rvSwipe.recyclerView.isLoading = true
        adapter?.notifyDataSetChanged()
        //_binding.spCenterName.setSelection(0)
        getCollectionList(page)
    }

    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(requireContext())
        _binding.rvSwipe.recyclerView.layoutManager = layoutManager
        adapter = CollectionAdapter(requireContext(), list, session, status, this)
        _binding.rvSwipe.recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: CollectionDataItem, action: String) {

        val intent = Intent(context, AddCollectionActivity::class.java)
        intent.putExtra(Constant.DATA, data)
        intent.putExtra(Constant.TYPE, status)
        startActivity(intent)
        Animatoo.animateCard(context)
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
                intent.putExtra(Constant.DATA, Constant.COLLECTION)
                intent.putExtra(Constant.TYPE, status)
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

    override fun onResume() {
        checkUserSatus()
        if (status == Constant.PENDING) {
            _binding.txtPending.isSelected = true
        } else if (status == Constant.ALL) {
            _binding.txtAll.isSelected = true
        } else if (status == Constant.COLLECTED) {
            _binding.txtCollected.isSelected = true
        } else {
            _binding.txtVisitDone.isSelected = true
        }
        getCenterNameList2(status)

        page = 1
        list.clear()
        hasNextPage = true
        _binding.rvSwipe.swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        _binding.rvSwipe.recyclerView.isLoading = true
        getCollectionList(page)
        // _binding.spCenterName.setSelection(0)
        super.onResume()

    }

    fun spinnerAPICall() {
        if (status == Constant.PENDING) {
            _binding.txtPending.isSelected = true
        } else if (status == Constant.ALL) {
            _binding.txtAll.isSelected = true
        } else {
            _binding.txtCollected.isSelected = true
        }
        page = 1
        list.clear()
        hasNextPage = true
        _binding.rvSwipe.swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        _binding.rvSwipe.recyclerView.isLoading = true
        getCollectionList(page)
    }


    fun spinnerAPICall2() {
        page = 1
        list.clear()
        hasNextPage = true
        _binding.rvSwipe.swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        _binding.rvSwipe.recyclerView.isLoading = true
        getCollectionList(page)
    }


    fun getCollectionList(page: Int) {
        val params = HashMap<String, Any>()
        params["PageSize"] = Constant.PAGE_SIZE
        params["CurrentPage"] = page

        if (checkUserIsBM(session.user.data?.userType!!)) {
            params["FECode"] = selectedFEId
        } else {
            params["FECode"] = session.user.data?.fECode.toString()
        }
        params["BMCode"] = session.user.data?.bMCode.toString()
        params["CenterName"] = CenterName
        params["LoanID"] = LoanID
        params["ClientID"] = ClientID
        params["ClientName"] = ClientName
        params["CollectionType"] = status
        if (!StartDate.isEmpty() && !EndDate.isEmpty()) {
            params["StartDate"] = StartDate
            params["EndDate"] = EndDate
        }

        Log.d("Request::::>", "getCollectionList: " + params)
        Networking
            .with(requireContext())
            .getServices()
            .getCollection(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CollectionListModal>() {
                override fun onSuccess(response: CollectionListModal) {
                    if (list.size > 0) {
                        _binding.rvSwipe.progressbar.invisible()
                    }
                    _binding.rvSwipe.swipeRefreshLayout.isRefreshing = false

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
                        _binding.rvSwipe.progressbar.invisible()
                    }
                    showAlert(getString(R.string.show_server_error))
                    refreshData(message, code)
                }

            }).addTo(autoDisposable)
    }


    private fun refreshData(msg: String?, code: Int) {
        _binding.rvSwipe.recyclerView.setLoadedCompleted()
        _binding.rvSwipe.swipeRefreshLayout.isRefreshing = false
        adapter?.notifyDataSetChanged()

        if (list.size > 0) {
            _binding.rvSwipe.imgNodata.invisible()
            _binding.rvSwipe.recyclerView.visible()
        } else {
            _binding.rvSwipe.imgNodata.visible()
            if (code == 0)
                _binding.rvSwipe.imgNodata.setImageResource(R.drawable.no_internet_bg)
            else
                _binding.rvSwipe.imgNodata.setImageResource(R.drawable.nodata)
            _binding.rvSwipe.recyclerView.invisible()
        }
    }

    override fun onDestroyView() {
        CenterName = ""
        ClientID = ""
        LoanID = ""
        ClientName = ""
        super.onDestroyView()
    }

    override fun onDestroy() {
        CenterName = ""
        ClientID = ""
        LoanID = ""
        ClientName = ""
        super.onDestroy()
    }

    private fun centerNameViewClick() {


        _binding.view2.setOnClickListener {
            itemCenterNameType?.let { it1 ->
                SearchableDialog(requireContext(),
                    it1,
                    getString(R.string.center_name), { item, _ ->
                        _binding.spCenterName.setSelection(item.id.toInt())
                    }).show()
            }

        }


    }

    private fun centerNameSpinnerListner() {
        _binding.spCenterName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && centerNameListArray!!.size > position - 1) {
                    if (position == 0) {
                        CenterName = ""
                        // spinnerAPICall2()

                    } else {
                        CenterName = centerNameListArray!!.get(position - 1).centerName.toString()
                        spinnerAPICall()
                    }
                    Logger.d("userIDq", CenterName)

                }

            }
        }
    }

    fun getCenterNameList(type: String) {
        centerNameListArray.clear()
        centerNameList.clear()
        val params = HashMap<String, Any>()
        params["Type"] = type
        params["FECode"] = session.user.data?.fECode.toString()
        params["BMCode"] = session.user.data?.bMCode.toString()
        Networking
            .with(requireContext())
            .getServices()
            .getCenterName(Networking.wrapParams(params))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CenternameListModal>() {
                override fun onSuccess(response: CenternameListModal) {
                    if (response.error == false) {
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
                            requireContext(),
                            R.layout.custom_spinner_item,
                            centerNameList!!
                        )
                        _binding.spCenterName.setAdapter(adaptercenterName)
                    } else {
                        var myList: MutableList<SearchableItem> = mutableListOf()
                        centerNameList!!.add(getString(R.string.center_name))


                        myList.add(SearchableItem(0, getString(R.string.center_name)))

                        itemCenterNameType = myList

                        adaptercenterName = ArrayAdapter(
                            requireContext(),
                            R.layout.custom_spinner_item,
                            centerNameList!!
                        )
                        _binding.spCenterName.setAdapter(adaptercenterName)
                    }



                    getRefreshData()
                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun getCenterNameList2(type: String) {
        centerNameListArray.clear()
        centerNameList.clear()
        val params = HashMap<String, Any>()
        params["Type"] = type
        params["FECode"] = session.user.data?.fECode.toString()
        params["BMCode"] = session.user.data?.bMCode.toString()
        Log.d("Request::::>", "getCenterName: " + params)
        Networking
            .with(requireContext())
            .getServices()
            .getCenterName(Networking.wrapParams(params))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CenternameListModal>() {
                override fun onSuccess(response: CenternameListModal) {
                    if (response.error == false) {
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
                            requireContext(),
                            R.layout.custom_spinner_item,
                            centerNameList!!
                        )
                        _binding.spCenterName.setAdapter(adaptercenterName)
                    } else {
                        var myList: MutableList<SearchableItem> = mutableListOf()
                        centerNameList!!.add(getString(R.string.center_name))


                        myList.add(SearchableItem(0, getString(R.string.center_name)))
                        itemCenterNameType = myList

                        adaptercenterName = ArrayAdapter(
                            requireContext(),
                            R.layout.custom_spinner_item,
                            centerNameList!!
                        )
                        _binding.spCenterName.setAdapter(adaptercenterName)
                    }


                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

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
                    showAlert(getString(R.string.show_server_error))
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

        Log.d("Request::::>", "getFEList: " + params)
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
                    _binding.spFEList.setAdapter(adapterFE)


                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }


    private fun FEViewClick() {


        _binding.viewFE.setOnClickListener {
            itemFEType?.let { it1 ->
                SearchableDialog(requireContext(),
                    it1,
                    getString(R.string.select_field_executive), { item, _ ->
                        _binding.spFEList.setSelection(item.id.toInt())
                    }).show()
            }

        }


    }

    private fun FESpinnerListner() {
        _binding.spFEList.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && FEListArray.size > position - 1) {
                    if (position == 0) {
                        //    CenterName = ""
                        // spinnerAPICall2()
                        // CenterName = FEListArray.get(position - 1).name.toString()
                        //   spinnerAPICall()
                        selectedFEId = ""

                    } else {
                        selectedFEId = FEListArray.get(position - 1).fECode.toString()
                    }

                    page = 1
                    list.clear()
                    hasNextPage = true
                    _binding.rvSwipe.swipeRefreshLayout.isRefreshing = true
                    setupRecyclerView()
                    _binding.rvSwipe.recyclerView.isLoading = true

                    getCollectionList(page)
                }
                Logger.d("fecode", selectedFEId)

            }

        }
    }
}


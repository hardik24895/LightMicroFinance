package com.lightmicrofinance.app.fragment


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.commonProject.interfaces.LoadMoreListener
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.activity.AddCollectionActivity
import com.lightmicrofinance.app.activity.SearchActivty
import com.lightmicrofinance.app.adapter.CollectionAdapter
import com.lightmicrofinance.app.databinding.FragmentCollectionBinding
import com.lightmicrofinance.app.extention.invisible
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.extention.visible
import com.lightmicrofinance.app.modal.CenternameDataItem
import com.lightmicrofinance.app.modal.CenternameListModal
import com.lightmicrofinance.app.modal.CollectionDataItem
import com.lightmicrofinance.app.modal.CollectionListModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import com.lightmicrofinance.app.utils.Logger
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
    var status = Constant.PENDING
    var page: Int = 1
    var hasNextPage: Boolean = true

    companion object {
        var CenterName: String = ""
        var LoanID: String = ""
        var ClientID: String = ""
        var ClientName: String = ""
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
        getCenterNameList(Constant.PENDING)
        centerNameSpinnerListner()
        centerNameViewClick()

        _binding.txtPending.setOnClickListener {
            if (!_binding.txtPending.isSelected) {
                _binding.txtPending.isSelected = true
                _binding.txtPartialy.isSelected = false
                _binding.txtCollected.isSelected = false
                status = Constant.PENDING
                getCenterNameList(Constant.PENDING)

            }

        }
        _binding.txtPartialy.setOnClickListener {
            if (!_binding.txtPartialy.isSelected) {
                _binding.txtPending.isSelected = false
                _binding.txtPartialy.isSelected = true
                _binding.txtCollected.isSelected = false
                status = Constant.PARTIALY
                getCenterNameList(Constant.PARTIALY)
                //getRefreshData()
            }


        }
        _binding.txtCollected.setOnClickListener {
            if (!_binding.txtCollected.isSelected) {
                _binding.txtPending.isSelected = false
                _binding.txtPartialy.isSelected = false
                _binding.txtCollected.isSelected = true
                status = Constant.COLLECTED
                getCenterNameList(Constant.COLLECTED)
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
            page = 1
            list.clear()
            hasNextPage = true
            _binding.rvSwipe.recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            _binding.spCenterName.setSelection(0)
            getCollectionList(page)
        }

    }

    fun getRefreshData() {
        CenterName = ""
        ClientID = ""
        LoanID = ""
        ClientName = ""
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
        if (status == Constant.PENDING) {
            _binding.txtPending.isSelected = true
        } else if (status == Constant.PARTIALY) {
            _binding.txtPartialy.isSelected = true
        } else {
            _binding.txtCollected.isSelected = true
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
        } else if (status == Constant.PARTIALY) {
            _binding.txtPartialy.isSelected = true
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
        params["FECode"] = session.user.data?.fECode.toString()
        params["BMCode"] = session.user.data?.bMCode.toString()
        params["CenterName"] = CenterName
        params["LoanID"] = LoanID
        params["ClientID"] = ClientID
        params["ClientName"] = ClientName
        params["CollectionType"] = status

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
        Networking
            .with(requireContext())
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
                        requireContext(),
                        R.layout.custom_spinner_item,
                        centerNameList!!
                    )
                    _binding.spCenterName.setAdapter(adaptercenterName)

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
        Networking
            .with(requireContext())
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
                        requireContext(),
                        R.layout.custom_spinner_item,
                        centerNameList!!
                    )
                    _binding.spCenterName.setAdapter(adaptercenterName)


                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }
}
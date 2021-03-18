package com.lightmicrofinance.commonproject.fragment


import android.os.Bundle
import android.view.*

import com.lightmicrofinance.commonproject.adapter.LeadAdapter
import com.lightmicrofinance.commonproject.databinding.ReclerviewSwipelayoutBinding


class LeadFragment : BaseFragment(), LeadAdapter.OnItemSelected {

    var adapter: LeadAdapter? = null

    private val list: MutableList<String> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true

    companion object {
        var email: String = ""
        var name: String = ""
    }

    private var _binding: ReclerviewSwipelayoutBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ReclerviewSwipelayoutBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
  /*      setHomeScreenTitle(requireActivity(), getString(R.string.nav_visitor))
        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getLeadList(++page)
                }
            }
        })*/

    /*    swipeRefreshLayout.setOnRefreshListener {
            email = ""
            name = ""
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getLeadList(page)
        }*/
    }

/*    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = LeadAdapter(requireContext(), list, session, this)
        recyclerView.adapter = adapter

    }*/

    override fun onItemSelect(position: Int, data: String, action: String) {


    }





/*    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        val filter = menu.findItem(R.id.action_filter)
        filter.setVisible(true)
        super.onCreateOptionsMenu(menu, inflater)
    }*/

/*    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                if (checkUserRole(
                        session.roleData.data?.visitor?.isEdit.toString(),
                        requireContext()
                    )
                )
                    showDialog()
                return true
            }
            R.id.action_filter -> {
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra(Constant.DATA, Constant.LEAD)
                startActivity(intent)
                Animatoo.animateCard(context)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

/*    override fun onResume() {
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getLeadList(page)
        super.onResume()

    }*/

/*
    fun showDialog() {
        val dialog = AddVisitorDailog.newInstance(requireContext(),
            object : AddVisitorDailog.onItemClick {
                override fun onItemCLicked(mobile: String, serviceId: String) {
                    checkLead(mobile, serviceId)
                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, getString(R.string.app_name))
//        bundle.putString(
//            Constant.TEXT,
//            getString(R.string.msg_get_data_from_server)
//        )
        dialog.arguments = bundle
        dialog.show(childFragmentManager, "YesNO")
    }
*/

   /* fun getLeadList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("Name", name)
            jsonBody.put("EmailID", email)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            result = Networking.setParentJsonData(
                Constant.METHOD_LEADLIST,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }


        Networking
            .with(requireContext())
            .getServices()
            .getLeadList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<LeadListModal>() {
                override fun onSuccess(response: LeadListModal) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    swipeRefreshLayout.isRefreshing = false
                    list.addAll(response.data)
                    adapter?.notifyItemRangeInserted(
                        list.size.minus(response.data.size),
                        list.size
                    )
                    hasNextPage = list.size < response.rowcount

                    refreshData(getString(R.string.no_data_found), 1)
                }

                override fun onFailed(code: Int, message: String) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    showAlert(message)
                    refreshData(message, code)
                }

            }).addTo(autoDisposable)
    }*/




    /*private fun refreshData(msg: String?, code: Int) {
        recyclerView.setLoadedCompleted()
        swipeRefreshLayout.isRefreshing = false
        adapter?.notifyDataSetChanged()

        if (list.size > 0) {
            imgNodata.invisible()
            recyclerView.visible()
        } else {
            imgNodata.visible()
            if (code == 0)
                imgNodata.setImageResource(R.drawable.no_internet_bg)
            else
                imgNodata.setImageResource(R.drawable.nodata)
            recyclerView.invisible()
        }
    }*/

    override fun onDestroyView() {
        email = ""
        name = ""
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        email = ""
        name = ""
        super.onDestroy()
    }



}
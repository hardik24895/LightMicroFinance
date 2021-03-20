package com.lightmicrofinance.commonproject.fragment


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.commonProject.extention.invisible
import com.commonProject.extention.showAlert
import com.commonProject.extention.visible
import com.commonProject.interfaces.LoadMoreListener
import com.commonProject.network.CallbackObserver
import com.commonProject.network.Networking
import com.commonProject.network.addTo
import com.commonProject.utils.Constant
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.activity.SearchActivty
import com.lightmicrofinance.commonproject.adapter.ParAdapter
import com.lightmicrofinance.commonproject.databinding.ReclerviewSwipelayoutBinding
import com.lightmicrofinance.commonproject.modal.ParDataItem
import com.lightmicrofinance.commonproject.modal.ParListModal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers



class ParFragment : BaseFragment(), ParAdapter.OnItemSelected {

    var adapter: ParAdapter? = null

    private val list: MutableList<ParDataItem> = mutableListOf()
    var status = Constant.PENDING
    var page: Int = 1
    var hasNextPage: Boolean = true
    companion object {
        var CenterName: String = ""
        var LoanID: String = ""
        var ClientID: String = ""
        var ClientName: String = ""
    }

    private var _binding: ReclerviewSwipelayoutBinding? = null

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

        //setupRecyclerView()

              _binding?.recyclerView?.setLoadMoreListener(object : LoadMoreListener {
                  override fun onLoadMore() {
                      if (hasNextPage && !   _binding?.recyclerView?.isLoading!!) {
                          _binding?.progressbar?.visible()
                          getParList(++page)
                      }
                  }
              })

        _binding?.swipeRefreshLayout?.setOnRefreshListener {
                CenterName = ""
                ClientID = ""
                LoanID = ""
                ClientName = ""
                page = 1
                list.clear()
                hasNextPage = true
                _binding?.recyclerView?.isLoading = true
                adapter?.notifyDataSetChanged()
                getParList(page)
            }

    }

    fun getRefreshData(){
        CenterName = ""
        ClientID = ""
        LoanID = ""
        ClientName = ""
        page = 1
        list.clear()
        setupRecyclerView()
        hasNextPage = true
      _binding?.swipeRefreshLayout?.isRefreshing = true
      _binding?.recyclerView?.isLoading = true
        adapter?.notifyDataSetChanged()
        getParList(page)
    }

    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(requireContext())
      _binding?.recyclerView?.layoutManager = layoutManager
        adapter = ParAdapter(requireContext(), list, session, status, this)
      _binding?.recyclerView?.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: ParDataItem, action: String) {

      // goToActivity<AddCollectionActivity>()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            /* R.id.action_add -> {
                 if (checkUserRole(
                         session.roleData.data?.visitor?.isEdit.toString(),
                         requireContext()
                     )
                 )
                     showDialog()
                 return true
             }*/
            R.id.action_filter -> {
                val intent = Intent(context, SearchActivty::class.java)
                intent.putExtra(Constant.DATA, Constant.PAR)
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
        page = 1
        list.clear()
        hasNextPage = true
      _binding?.swipeRefreshLayout?.isRefreshing = true
        setupRecyclerView()
      _binding?.recyclerView?.isLoading = true
        getParList(page)
        super.onResume()

    }


     fun getParList(page: Int) {

         val params = HashMap<String, Any>()
         params["PageSize"] =  Constant.PAGE_SIZE
         params["CurrentPage"] = page
         params["FECode"] = session.user.data?.fECode.toString()
         params["CenterName"] = CenterName
         params["LoanID"] = LoanID
         params["ClientID"] = ClientID
         params["ClientName"] = ClientName

         Networking
             .with(requireContext())
             .getServices()
             .getPar(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribeWith(object : CallbackObserver<ParListModal>() {
                 override fun onSuccess(response: ParListModal) {
                     if (list.size > 0) {
                       _binding?.progressbar?.invisible()
                     }
                   _binding?.swipeRefreshLayout?.isRefreshing = false

                     if (response.error==false){
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

    override fun onDestroyView() {
        CenterName = ""
        ClientID = ""
        LoanID = ""
        ClientName = ""
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        CenterName = ""
        ClientID = ""
        LoanID = ""
        ClientName = ""
        super.onDestroy()
    }


}
package com.lightmicrofinance.commonproject.fragment


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.commonProject.extention.goToActivity
import com.commonProject.extention.invisible
import com.commonProject.extention.showAlert
import com.commonProject.extention.visible
import com.commonProject.interfaces.LoadMoreListener
import com.commonProject.network.CallbackObserver
import com.commonProject.network.Networking
import com.commonProject.network.addTo
import com.commonProject.utils.Constant
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.activity.AddCollectionActivity
import com.lightmicrofinance.commonproject.activity.SearchActivty
import com.lightmicrofinance.commonproject.adapter.CollectionAdapter
import com.lightmicrofinance.commonproject.databinding.FragmentCollectionBinding
import com.lightmicrofinance.commonproject.modal.CollectionDataItem
import com.lightmicrofinance.commonproject.modal.CollectionListModal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import org.json.JSONObject


class CollectionFragment : BaseFragment(), CollectionAdapter.OnItemSelected {

    var adapter: CollectionAdapter? = null

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

    private var _binding: FragmentCollectionBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setupRecyclerView()


        
        _binding?.txtPending?.setOnClickListener {
            _binding?.txtPending?.isSelected = true
            _binding?.txtPartialy?.isSelected = false
            _binding?.txtCollected?.isSelected = false
            status = Constant.PENDING
           // setupRecyclerView()
            getRefreshData()
        }
        _binding?.txtPartialy?.setOnClickListener {
            _binding?.txtPending?.isSelected = false
            _binding?.txtPartialy?.isSelected = true
            _binding?.txtCollected?.isSelected = false
            status =Constant.PARTIALY

            getRefreshData()
         
        }
        _binding?.txtCollected?.setOnClickListener {
            _binding?.txtPending?.isSelected = false
            _binding?.txtPartialy?.isSelected = false
            _binding?.txtCollected?.isSelected = true
            status =Constant.COLLECTED
           // setupRecyclerView()
            getRefreshData()
        }

              _binding?.rvSwipe?.recyclerView?.setLoadMoreListener(object : LoadMoreListener {
                  override fun onLoadMore() {
                      if (hasNextPage && !   _binding?.rvSwipe?.recyclerView?.isLoading!!) {
                          _binding?.rvSwipe?.progressbar?.visible()
                          getCollectionList(++page)
                      }
                  }
              })

        _binding?.rvSwipe?.swipeRefreshLayout?.setOnRefreshListener {
                CenterName = ""
                ClientID = ""
                LoanID = ""
                ClientName = ""
                page = 1
                list.clear()
                hasNextPage = true
                _binding?.rvSwipe?.recyclerView?.isLoading = true
                adapter?.notifyDataSetChanged()
                getCollectionList(page)
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
        _binding?.rvSwipe?.swipeRefreshLayout?.isRefreshing = true
        _binding?.rvSwipe?.recyclerView?.isLoading = true
        adapter?.notifyDataSetChanged()
        getCollectionList(page)
    }

    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(requireContext())
        _binding?.rvSwipe?.recyclerView?.layoutManager = layoutManager
        adapter = CollectionAdapter(requireContext(), list, session, status, this)
        _binding?.rvSwipe?.recyclerView?.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: CollectionDataItem, action: String) {

       goToActivity<AddCollectionActivity>()
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
        if (status==Constant.PENDING){
            _binding?.txtPending?.isSelected = true
        }else if (status == Constant.PARTIALY){
            _binding?.txtPartialy?.isSelected = true
        }else{
            _binding?.txtCollected?.isSelected = true
        }
        page = 1
        list.clear()
        hasNextPage = true
        _binding?.rvSwipe?.swipeRefreshLayout?.isRefreshing = true
        setupRecyclerView()
        _binding?.rvSwipe?.recyclerView?.isLoading = true
        getCollectionList(page)


        super.onResume()

    }


     fun getCollectionList(page: Int) {


         val params = HashMap<String, Any>()
         params["PageSize"] =  Constant.PAGE_SIZE
         params["CurrentPage"] = page
         params["FECode"] = "LMF03103"
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
                         _binding?.rvSwipe?.progressbar?.invisible()
                     }
                     _binding?.rvSwipe?.swipeRefreshLayout?.isRefreshing = false

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
                         _binding?.rvSwipe?.progressbar?.invisible()
                     }
                     showAlert(getString(R.string.show_server_error))
                     refreshData(message, code)
                 }

             }).addTo(autoDisposable)
     }


    private fun refreshData(msg: String?, code: Int) {
        _binding?.rvSwipe?.recyclerView?.setLoadedCompleted()
        _binding?.rvSwipe?.swipeRefreshLayout?.isRefreshing = false
        adapter?.notifyDataSetChanged()

        if (list.size > 0) {
            _binding?.rvSwipe?.imgNodata?.invisible()
            _binding?.rvSwipe?.recyclerView?.visible()
        } else {
            _binding?.rvSwipe?.imgNodata?.visible()
            if (code == 0)
                _binding?.rvSwipe?.imgNodata?.setImageResource(R.drawable.ic_login_logo)
            else
                _binding?.rvSwipe?.imgNodata?.setImageResource(R.drawable.ic_login_logo)
            _binding?.rvSwipe?.recyclerView?.invisible()
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
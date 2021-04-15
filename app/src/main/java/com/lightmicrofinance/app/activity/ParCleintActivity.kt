package com.lightmicrofinance.app.activity


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.commonProject.interfaces.LoadMoreListener
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.adapter.ParAdapter
import com.lightmicrofinance.app.databinding.ActivityParCleintListBinding
import com.lightmicrofinance.app.extention.invisible
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.extention.visible
import com.lightmicrofinance.app.modal.ParDataItem
import com.lightmicrofinance.app.modal.ParListModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ParCleintActivity : BaseActivity(), ParAdapter.OnItemSelected {

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
        var BucketSize: String = ""
    }

    lateinit var binding: ActivityParCleintListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityParCleintListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (!intent.getStringExtra(Constant.BUCKET_SIZE).toString().isEmpty()) {
            binding.include.txtTitle.text =
                getString(R.string.par) + " (" + intent.getStringExtra(Constant.BUCKET_SIZE) + ")"
        } else {
            binding.include.txtTitle.text = getString(R.string.par) + " (Total)"
        }


        binding.include.imgBack.setOnClickListener { finish() }

        binding.include2.recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !binding.include2.recyclerView.isLoading) {
                    binding.include2.progressbar.visible()
                    getParList(++page)
                }
            }
        })


    }


    fun setupRecyclerView() {

        val layoutManager = LinearLayoutManager(this)
        binding.include2.recyclerView.layoutManager = layoutManager
        adapter = ParAdapter(this, list, session, status, this)
        binding.include2.recyclerView.adapter = adapter

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
                val intent = Intent(this, SearchActivty::class.java)
                intent.putExtra(Constant.DATA, Constant.PAR)
                startActivity(intent)
                Animatoo.animateCard(this)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onResume() {
        page = 1
        list.clear()
        hasNextPage = true
        setupRecyclerView()
        binding.include2.recyclerView.isLoading = true
        BucketSize = intent.getStringExtra(Constant.BUCKET_SIZE).toString()
        getParList(page)
        super.onResume()

    }


    fun getParList(page: Int) {

        val params = HashMap<String, Any>()
        params["PageSize"] = Constant.PAGE_SIZE
        params["CurrentPage"] = page
        params["FECode"] = session.user.data?.fECode.toString()
        params["BMCode"] = session.user.data?.bMCode.toString()
        params["CenterName"] = CenterName
        params["LoanID"] = LoanID
        params["ClientID"] = ClientID
        params["ClientName"] = ClientName
        params["Bucket"] = BucketSize

        Networking
            .with(this)
            .getServices()
            .getPar(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<ParListModal>() {
                override fun onSuccess(response: ParListModal) {
                    if (list.size > 0) {
                        binding.include2.progressbar.invisible()
                    }


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
                        binding.include2.progressbar.invisible()
                    }
                    showAlert(getString(R.string.show_server_error))
                    refreshData(message, code)
                }

            }).addTo(autoDisposable)
    }


    private fun refreshData(msg: String?, code: Int) {
        binding.include2.recyclerView.setLoadedCompleted()
        adapter?.notifyDataSetChanged()
        if (list.size > 0) {
            binding.include2.imgNodata.invisible()
            binding.include2.recyclerView.visible()
        } else {
            binding.include2.imgNodata.visible()
            if (code == 0)
                binding.include2.imgNodata.setImageResource(R.drawable.no_internet_bg)
            else
                binding.include2.imgNodata.setImageResource(R.drawable.nodata)
            binding.include2.recyclerView.invisible()
        }
    }


    override fun onDestroy() {
        CenterName = ""
        ClientID = ""
        LoanID = ""
        ClientName = ""
        BucketSize = ""
        super.onDestroy()
    }


}
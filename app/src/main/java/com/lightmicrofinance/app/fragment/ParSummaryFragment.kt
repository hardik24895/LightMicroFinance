package com.lightmicrofinance.app.fragment


import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.activity.ParCleintActivity
import com.lightmicrofinance.app.activity.SearchActivty
import com.lightmicrofinance.app.databinding.FragementSummaryParBinding
import com.lightmicrofinance.app.extention.getYesterdayDate
import com.lightmicrofinance.app.extention.invisible
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.extention.visible
import com.lightmicrofinance.app.modal.FEDataItem
import com.lightmicrofinance.app.modal.FEDateModel
import com.lightmicrofinance.app.modal.ParSummaryModal
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


class ParSummaryFragment : BaseFragment() {

    private var _binding: FragementSummaryParBinding? = null

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
        _binding = FragementSummaryParBinding.inflate(inflater, container, false)
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

        binding.txt130Cleints.setOnClickListener {
            openCleintParList(Constant.oneTO30)
        }
        binding.txt3160Cleints.setOnClickListener {
            openCleintParList(Constant.threoneTO60)
        }

        binding.txt6190Cleints.setOnClickListener {
            openCleintParList(Constant.sixoneTO90)
        }

        binding.txt91180Cleints.setOnClickListener {
            openCleintParList(Constant.nineoneTO180)
        }

        binding.txt180AboveCleints.setOnClickListener {
            openCleintParList(Constant.oneeightaboveTO180)
        }

        binding.txtGTotalCleints.setOnClickListener {
            openCleintParList("")
        }


        binding.txt130.setOnClickListener {
            openCleintParList(Constant.oneTO30)
        }
        binding.txt3160.setOnClickListener {
            openCleintParList(Constant.threoneTO60)
        }

        binding.txt6190.setOnClickListener {
            openCleintParList(Constant.sixoneTO90)
        }

        binding.txt91180.setOnClickListener {
            openCleintParList(Constant.nineoneTO180)
        }

        binding.txt180Above.setOnClickListener {
            openCleintParList(Constant.oneeightaboveTO180)
        }

        binding.txtGTotal.setOnClickListener {
            openCleintParList("")
        }

    }

    override fun onResume() {
        super.onResume()
        binding.txtSelectedDate.text = StartDate + " To " + EndDate
        getSummaryData()
    }

    fun openCleintParList(bucketSize: String) {
        val intent = Intent(requireContext(), ParCleintActivity::class.java)
        intent.putExtra(Constant.DATA, Constant.BUCKET)
        intent.putExtra(Constant.BUCKET_SIZE, bucketSize)
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
                intent.putExtra(Constant.DATA, Constant.PAR_SUMMARY)
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
            .getParSummary(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<ParSummaryModal>() {
                override fun onSuccess(response: ParSummaryModal) {
                    val data = response.data
                    hideProgressbar()
                    if (response.error == false) {

                        binding.txt130Cleints.setPaintFlags(binding.txt130Cleints.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                        binding.txt130Cleints.text = data?.jsonMember130?.clients
                        binding.txt130ParAmount.text = data?.jsonMember130?.amount
                        binding.txt130ODAmount.text = data?.jsonMember130?.overdueAmount

                        binding.txt3160Cleints.setPaintFlags(binding.txt3160Cleints.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                        binding.txt3160Cleints.text = data?.jsonMember3160?.clients.toString()
                        binding.txt3160ParAmount.text = data?.jsonMember3160?.amount.toString()
                        binding.txt3160ODAmount.text =
                            data?.jsonMember3160?.overdueAmount.toString()

                        binding.txt6190Cleints.setPaintFlags(binding.txt6190Cleints.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                        binding.txt6190Cleints.text = data?.jsonMember6190?.clients.toString()
                        binding.txt6190ParAmount.text = data?.jsonMember6190?.amount.toString()
                        binding.txt6190ODAmount.text =
                            data?.jsonMember6190?.overdueAmount.toString()

                        binding.txt91180Cleints.setPaintFlags(binding.txt91180Cleints.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                        binding.txt91180Cleints.text = data?.jsonMember91180?.clients.toString()
                        binding.txt91180ParAmount.text = data?.jsonMember91180?.amount.toString()
                        binding.txt91180ODAmount.text =
                            data?.jsonMember91180?.overdueAmount.toString()

                        binding.txt180AboveCleints.setPaintFlags(binding.txt180AboveCleints.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                        binding.txt180AboveCleints.text = data?.jsonMember180?.clients.toString()
                        binding.txt180AboveParAmount.text = data?.jsonMember180?.amount.toString()
                        binding.txt180AboveOdAmount.text =
                            data?.jsonMember180?.overdueAmount.toString()

                        binding.txtGTotalCleints.setPaintFlags(binding.txtGTotalCleints.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
                        binding.txtGTotalCleints.text = data?.total?.clients.toString()
                        binding.txtGTotalParAmount.text = data?.total?.amount.toString()
                        binding.txtGTotalOdAmount.text = data?.total?.overdueAmount.toString()


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
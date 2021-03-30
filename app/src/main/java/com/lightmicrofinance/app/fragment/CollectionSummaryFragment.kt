package com.lightmicrofinance.app.fragment


import android.content.Intent
import android.os.Bundle
import android.view.*
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.activity.SearchActivty
import com.lightmicrofinance.app.databinding.FragamentCollectionSummaryBinding
import com.lightmicrofinance.app.extention.getYesterdayDate
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.modal.CollectionSummaryReportModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import com.lightmicrofinance.app.utils.TimeStamp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class CollectionSummaryFragment : BaseFragment() {

    private var _binding: FragamentCollectionSummaryBinding? = null

    private val binding get() = _binding!!

    companion object {
        var StartDate: String = TimeStamp.getSpesificStartDateRange()
        var EndDate: String = getYesterdayDate()

    }

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

        binding.txtSelectedDate.text = StartDate + " To " + EndDate

    }

    override fun onResume() {
        super.onResume()
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
        params["StartDate"] = StartDate
        params["EndDate"] = EndDate

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
}
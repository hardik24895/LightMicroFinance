package com.lightmicrofinance.commonproject.fragment


import android.content.Intent
import android.os.Bundle
import android.view.*
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.commonProject.extention.getCurrentDate
import com.commonProject.extention.showAlert
import com.commonProject.network.CallbackObserver
import com.commonProject.network.Networking
import com.commonProject.network.addTo
import com.commonProject.utils.Constant
import com.commonProject.utils.TimeStamp
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.activity.SearchActivty
import com.lightmicrofinance.commonproject.databinding.FragementSummaryBusinessBinding
import com.lightmicrofinance.commonproject.modal.BusinessSummaryModal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class BusinessSummaryFragment : BaseFragment() {

    private var _binding: FragementSummaryBusinessBinding? = null

    private val binding get() = _binding!!

    companion object {
        var StartDate: String = TimeStamp.getStartDateRange()
        var EndDate: String = getCurrentDate()

    }

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
                        binding.txtLENewAchived.text = data?.lENew?.percentage

                        binding.txtLEReNewPlan.text = data?.lERenew?.lERenew
                        binding.txtLEReNewActual.text = data?.lERenew?.achLERenew
                        binding.txtLEReNewDifferent.text = data?.lERenew?.diff
                        binding.txtLEReNewAchived.text = data?.lERenew?.percentage

                        binding.txtTotalLEPlan.text = data?.totalLE?.totalLE
                        binding.txtTotalLEActual.text = data?.totalLE?.achTotalLE
                        binding.txtTotalLEDifferent.text = data?.totalLE?.diff
                        binding.txtTotalLEAchived.text = data?.lENew?.percentage

                        binding.txtDDPlan.text = data?.dDDone?.dDDone
                        binding.txtDDActual.text = data?.dDDone?.achDDDone
                        binding.txtDDDifferent.text = data?.dDDone?.diff
                        binding.txtDDAchived.text = data?.dDDone?.percentage

                        binding.txtDDvePlan.text = data?.dDPositive?.dDPositive
                        binding.txtDDveActual.text = data?.dDPositive?.achDDPositive
                        binding.txtDDveDifferent.text = data?.dDPositive?.diff
                        binding.txtDDveAchived.text = data?.dDPositive?.percentage

                        binding.txtGRTPlan.text = data?.gRT?.gRT
                        binding.txtGRTActual.text = data?.gRT?.achGRT
                        binding.txtGRTDifferent.text = data?.gRT?.diff
                        binding.txtGRTAchived.text = data?.gRT?.percentage

                        binding.textDistClientPlan.text = data?.disbClient?.disbClient
                        binding.textDistClientActual.text = data?.disbClient?.achDisbClient
                        binding.textDistClientDifferent.text = data?.disbClient?.diff
                        binding.textDistClientAchived.text = data?.disbClient?.percentage

                        binding.textDistAmountPlan.text = data?.disbAmount?.disbAmount
                        binding.textDistAmountActual.text = data?.disbAmount?.achDisbAmount
                        binding.textDistAmountDifferent.text = data?.disbAmount?.diff
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
}
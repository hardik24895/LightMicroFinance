package com.lightmicrofinance.commonproject.fragment


import android.content.Intent
import android.os.Bundle
import android.view.*
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.commonProject.extention.getYesterdayDate
import com.commonProject.extention.showAlert
import com.commonProject.network.CallbackObserver
import com.commonProject.network.Networking
import com.commonProject.network.addTo
import com.commonProject.utils.Constant
import com.commonProject.utils.TimeStamp
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.activity.SearchActivty
import com.lightmicrofinance.commonproject.databinding.FragmentGoalsheetBinding
import com.lightmicrofinance.commonproject.modal.BusinessSummaryModal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class GoalSheetFragment : BaseFragment() {

    private var _binding: FragmentGoalsheetBinding? = null

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
        _binding = FragmentGoalsheetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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
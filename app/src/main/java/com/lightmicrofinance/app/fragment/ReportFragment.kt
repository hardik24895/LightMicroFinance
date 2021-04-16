package com.lightmicrofinance.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.activity.LoginActivity
import com.lightmicrofinance.app.activity.MainActivity
import com.lightmicrofinance.app.adapter.ViewPagerPagerAdapter
import com.lightmicrofinance.app.databinding.FragmentReportBinding
import com.lightmicrofinance.app.extention.goToActivityAndClearTask
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.modal.UserStatusModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ReportFragment : BaseFragment() {

    private var _binding: FragmentReportBinding? = null

    private val binding get() = _binding!!

    lateinit var viewPageradapter: ViewPagerPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatePageAdapter()

        (mContext as MainActivity).txtTitle.text =
            requireActivity().getString(R.string.collection_summary)

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    (mContext as MainActivity).txtTitle.text =
                        requireActivity().getString(R.string.collection_summary)
                } else if (position == 1) {
                    (mContext as MainActivity).txtTitle.text =
                        requireActivity().getString(R.string.business_summary)
                } else {
                    (mContext as MainActivity).txtTitle.text =
                        requireActivity().getString(R.string.par_summary)
                }

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })
    }

    private fun setStatePageAdapter() {
        viewPageradapter = ViewPagerPagerAdapter(childFragmentManager)
        // viewPageradapter.addFragment(CollectionChartFragment(), "Chart")
        viewPageradapter.addFragment(
            CollectionSummaryFragment(),
            getString(R.string.collection_summary)
        )
        viewPageradapter.addFragment(
            BusinessSummaryFragment(),
            getString(R.string.business_summary)
        )
        viewPageradapter.addFragment(ParSummaryFragment(), getString(R.string.par_summary))

        _binding?.viewPager?.adapter = viewPageradapter
        _binding?.tabs?.setupWithViewPager(_binding?.viewPager!!, true)

    }

    override fun onResume() {
        super.onResume()
        checkUserSatus()
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
                            if (data.status == "0")
                                goToActivityAndClearTask<LoginActivity>()
                        } else {
                            showAlert(response.message.toString())
                        }
                    } else {
                        showAlert(response.message.toString())
                    }

                }

                override fun onFailed(code: Int, message: String) {
                    showAlert(message)
                }

            }).addTo(autoDisposable)
    }
}
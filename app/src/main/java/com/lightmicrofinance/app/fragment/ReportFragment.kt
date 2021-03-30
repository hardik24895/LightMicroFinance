package com.lightmicrofinance.app.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.activity.MainActivity
import com.lightmicrofinance.app.adapter.ViewPagerPagerAdapter
import com.lightmicrofinance.app.databinding.FragmentReportBinding


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


}
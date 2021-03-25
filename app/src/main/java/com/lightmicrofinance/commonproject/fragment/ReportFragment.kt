package com.lightmicrofinance.commonproject.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lightmicrofinance.commonproject.adapter.ViewPagerPagerAdapter
import com.lightmicrofinance.commonproject.databinding.FragmentReportBinding


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
    }

    private fun setStatePageAdapter() {
        viewPageradapter = ViewPagerPagerAdapter(childFragmentManager)
       // viewPageradapter.addFragment(CollectionChartFragment(), "Chart")
        viewPageradapter.addFragment(BusinessSummaryFragment(), "B Summary")
        viewPageradapter.addFragment(ParSummaryFragment(), "Par Summary")
        viewPageradapter.addFragment(CollectionSummaryFragment(), "C Summary")
        _binding?.viewPager?.adapter = viewPageradapter
        _binding?.tabs?.setupWithViewPager(_binding?.viewPager!!, true)

    }


}
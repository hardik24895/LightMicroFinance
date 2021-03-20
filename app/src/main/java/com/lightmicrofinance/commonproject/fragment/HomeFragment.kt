package com.lightmicrofinance.commonproject.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.activity.MainActivity
import com.lightmicrofinance.commonproject.databinding.FragmentHomeBinding


class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // getTargetData()

        (activity as MainActivity).toolbar1.setBackgroundColor(resources.getColor(R.color.black))

        // activity.toolbar.setBackgroundColor(resources.getColor(R.color.black))

    }



}
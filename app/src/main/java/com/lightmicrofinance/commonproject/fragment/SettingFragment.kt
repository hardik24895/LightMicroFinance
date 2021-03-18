package com.lightmicrofinance.commonproject.fragment


import android.content.Intent
import android.os.Bundle
import android.view.*
import com.commonProject.utils.Constant
import com.lightmicrofinance.commonproject.activity.ChangePasswordActivity
import com.lightmicrofinance.commonproject.activity.InformationActivity
import com.lightmicrofinance.commonproject.databinding.FragmentSettingBinding



class SettingFragment : BaseFragment() {

    private var _binding: FragmentSettingBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val intent = Intent(requireContext(), InformationActivity::class.java)

        _binding?.linAboutus?.setOnClickListener {
            intent.putExtra(Constant.TITLE, "2")
            intent.putExtra("Desc", "AboutUS")
            mContext?.startActivity(intent)
        }
        _binding?.linPrivacyPolicy?.setOnClickListener {
            intent.putExtra(Constant.TITLE, "3")
            intent.putExtra("Desc", "PrivacyPolicy")
            mContext?.startActivity(intent)
        }
        _binding?.linTermsAndCondition?.setOnClickListener {
            intent.putExtra(Constant.TITLE, "1")
            intent.putExtra("Desc", "TermandCondition")
            startActivity(intent)
        }

        _binding?.linChangePassword?.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            intent.putExtra(Constant.TITLE, "1")
            intent.putExtra("Desc", "TermandCondition")
            startActivity(intent)
        }
    }

}
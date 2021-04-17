package com.lightmicrofinance.app.fragment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lightmicrofinance.app.activity.ChangePasswordActivity
import com.lightmicrofinance.app.activity.InformationActivity
import com.lightmicrofinance.app.activity.LoginActivity
import com.lightmicrofinance.app.databinding.FragmentSettingBinding
import com.lightmicrofinance.app.extention.goToActivityAndClearTask
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.modal.UserStatusModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


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
            intent.putExtra("Desc", "About US")
            mContext?.startActivity(intent)
        }
        _binding?.linPrivacyPolicy?.setOnClickListener {
            intent.putExtra(Constant.TITLE, "3")
            intent.putExtra("Desc", "Privacy Policy")
            mContext?.startActivity(intent)
        }
        _binding?.linTermsAndCondition?.setOnClickListener {
            intent.putExtra(Constant.TITLE, "1")
            intent.putExtra("Desc", "Term and Condition")
            startActivity(intent)
        }

        _binding?.linChangePassword?.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            intent.putExtra(Constant.TITLE, "1")
            intent.putExtra("Desc", "TermandCondition")
            startActivity(intent)
        }
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
                            if (data.status == "0") {
                                session.isLoggedIn = false
                                goToActivityAndClearTask<LoginActivity>()
                            }
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
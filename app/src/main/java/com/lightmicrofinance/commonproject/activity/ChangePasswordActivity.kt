package com.lightmicrofinance.commonproject.activity


import android.os.Bundle
import com.commonProject.extention.*
import com.commonProject.network.CallbackObserver
import com.commonProject.network.Networking
import com.commonProject.network.addTo
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.databinding.ActivityChangePasswordBinding
import com.lightmicrofinance.commonproject.modal.LoginModal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ChangePasswordActivity : BaseActivity() {
    lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.includes.txtTitle.text = getString(R.string.change_pwd)

        binding.includes.imgBack.setOnClickListener { finish() }

        binding.btnSubmit.setOnClickListener { validation() }
    }

    fun validation() {
        when {

            binding.edtOldPwd.isEmpty() -> {
                binding.mainView.showSnackBar(getString(R.string.enter_old_pwd))
            }

            binding.edtNewPassword.getValue().length < 6 -> {
                binding.mainView.showSnackBar(getString(R.string.enter_valid_password))
            }

            binding.edtConfirmPassword.getValue() != binding.edtNewPassword.getValue() -> {
                binding.mainView.showSnackBar(getString(R.string.password_not_matched))
            }

            else -> {
                changePassword()
            }

        }
    }

    fun changePassword() {
        showProgressbar()
        val params = HashMap<String, Any>()
        params["FECode"] = session.user.data?.fECode.toString()
        params["Password"] = binding.edtOldPwd.getValue()
        params["NewPassword"] = binding.edtNewPassword.getValue()

        Networking
            .with(this)
            .getServices()
            .login(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<LoginModal>() {
                override fun onSuccess(response: LoginModal) {
                    val data = response.data
                    hideProgressbar()
                    if (response.error == false) {
                        if (data != null) {
                            session.clearSession()
                            goToActivityAndClearTask<LoginActivity>()
                            binding.mainView.showSnackBar(response.message.toString())
                        } else {
                            showAlert(response.message.toString())
                        }
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
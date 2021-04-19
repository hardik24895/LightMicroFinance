package com.lightmicrofinance.app.activity


import android.os.Bundle
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.databinding.ActivityChangePasswordBinding
import com.lightmicrofinance.app.extention.*
import com.lightmicrofinance.app.modal.LoginModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
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

            binding.edtOldPwd.getValue() == binding.edtNewPassword.getValue() -> {
                binding.mainView.showSnackBar(getString(R.string.password_same_old_new))
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
        params["BMCode"] = session.user.data?.bMCode.toString()
        params["Password"] = binding.edtOldPwd.getValue()
        params["NewPassword"] = binding.edtNewPassword.getValue()

        Networking
            .with(this)
            .getServices()
            .changePassword(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
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
                    showAlert(getString(R.string.show_server_error))
                    hideProgressbar()
                }

            }).addTo(autoDisposable)
    }
}
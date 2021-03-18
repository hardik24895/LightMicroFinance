package com.lightmicrofinance.commonproject.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import com.commonProject.extention.*
import com.commonProject.network.CallbackObserver
import com.commonProject.network.Networking
import com.commonProject.network.addTo
import com.commonProject.utils.Constant
import com.commonProject.utils.SessionManager
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.databinding.ActivityLoginBinding
import com.lightmicrofinance.commonproject.dialog.ForgotPasswordDailog
import com.lightmicrofinance.commonproject.dialog.LogoutDailog
import com.lightmicrofinance.commonproject.modal.LoginModal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener { validation() }

        binding.edtPassword.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (event != null && event.keyCode === KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                validation()
            }
            false
        })

        binding.txtForgotpwd.setOnClickListener {
            val dialog1 = ForgotPasswordDailog.newInstance(
                this,
                object : ForgotPasswordDailog.onItemClick {
                    override fun onItemCLicked(pwd :String) {
                       showResetPassword(pwd)
                    }
                })
            val bundle = Bundle()
            bundle.putString(Constant.TITLE, this.getString(R.string.app_name))
            bundle.putString(Constant.TEXT, this.getString(R.string.msg_logout))
            dialog1.arguments = bundle
            dialog1.show(supportFragmentManager, "YesNO")
        }


    }

    fun showResetPassword(pwd :String){
        val dialog = LogoutDailog.newInstance(
            this,
            object : LogoutDailog.onItemClick {
                override fun onItemCLicked() {

                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, this.getString(R.string.app_name))
        bundle.putString(Constant.TEXT, "Your New Password " + pwd)
        bundle.putString(Constant.PASSWORD, "p")
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "YesNO")
    }

    fun validation() {
        when {
            binding.edtEmpId.isEmpty() -> {
                binding.mainView.showSnackBar(getString(R.string.enter_empid))
            }
            binding.edtEmpId.getValue().length != 8 -> {
                binding.mainView.showSnackBar(getString(R.string.enter_valid_empid))
            }
            binding.edtPassword.getValue().length < 6 -> {
                binding.mainView.showSnackBar(getString(R.string.enter_valid_password))
            }

            else -> {
                login()
            }


        }
    }

    fun login() {
        showProgressbar()
        val params = HashMap<String, Any>()
        params["FECode"] = binding.edtEmpId.getValue()
        params["Password"] = binding.edtPassword.getValue()

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
                            session.user = response
                            goToActivityAndClearTask<MainActivity>()
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
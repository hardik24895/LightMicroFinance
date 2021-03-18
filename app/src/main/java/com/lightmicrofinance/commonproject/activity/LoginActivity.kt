package com.lightmicrofinance.commonproject.activity

import android.os.Bundle
import com.commonProject.extention.goToActivityAndClearTask
import com.commonProject.utils.Constant
import com.commonProject.utils.SessionManager
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.databinding.ActivityAddCollectionBinding
import com.lightmicrofinance.commonproject.databinding.ActivityLoginBinding
import com.lightmicrofinance.commonproject.dialog.ForgotPasswordDailog
import com.lightmicrofinance.commonproject.dialog.LogoutDailog

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener { goToActivityAndClearTask<MainActivity>() }


        binding.txtForgotpwd.setOnClickListener {
            val dialog = ForgotPasswordDailog.newInstance(
                this,
                object : ForgotPasswordDailog.onItemClick {
                    override fun onItemCLicked() {
                        goToActivityAndClearTask<LoginActivity>()
                    }
                })
            val bundle = Bundle()
            bundle.putString(Constant.TITLE, this.getString(R.string.app_name))
            bundle.putString(Constant.TEXT, this.getString(R.string.msg_logout))
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "YesNO")
        }
    }
}
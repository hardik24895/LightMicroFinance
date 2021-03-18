package com.lightmicrofinance.commonproject.activity


import android.os.Bundle
import com.lightmicrofinance.commonproject.databinding.ActivityChangePasswordBinding


class ChangePasswordActivity : BaseActivity() {
    lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.includes.txtTitle.text = "Change Password"

        binding.includes.imgBack.setOnClickListener { finish() }
    }
}
package com.lightmicrofinance.commonproject.activity


import android.os.Bundle
import com.lightmicrofinance.commonproject.databinding.ActivityAddCollectionBinding


class AddCollectionActivity : BaseActivity() {
    lateinit var binding: ActivityAddCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCollectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.includes.txtTitle.text = "Collection"

        binding.includes.imgBack.setOnClickListener { finish() }
    }
}
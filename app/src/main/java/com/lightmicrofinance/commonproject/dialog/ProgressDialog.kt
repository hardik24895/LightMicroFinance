package com.commonProject.ui.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.ProgressBar
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.databinding.CommonDialogProgressBinding


/**
 * Created by Hardik
 */

class ProgressDialog @JvmOverloads constructor(
    context: Context,
    private var message: String? = null
) :
    AlertDialog(context, R.style.ProgressDialog) {

    lateinit var binding: CommonDialogProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CommonDialogProgressBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
       // setContentView(R.layout.common_dialog_progress)
        setCanceledOnTouchOutside(false)
        setCancelable(false)

        if (message != null) binding.tvMessage.text = message
    }

    fun updateMessage(message: String) {
        binding.tvMessage.text = message
    }

    fun getProgressBar(): ProgressBar {
        return binding.progressBar
    }
}

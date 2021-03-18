package com.lightmicrofinance.commonproject.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.commonProject.extention.invisible
import com.commonProject.extention.visible
import com.commonProject.network.AutoDisposable
import com.commonProject.utils.Constant
import com.commonProject.utils.SessionManager
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.databinding.DialogForgotPasswordBinding
import com.lightmicrofinance.commonproject.databinding.DialogLogoutBinding
import com.lightmicrofinance.commonproject.utils.BlurDialogFragment


class ForgotPasswordDailog(context: Context) : BlurDialogFragment(), LifecycleOwner {
    private val autoDisposable = AutoDisposable()
    private lateinit var session: SessionManager
    private var _binding: DialogForgotPasswordBinding? = null

    private val binding get() = _binding!!

    companion object {
        private lateinit var listener: onItemClick
        fun newInstance(
            context: Context,
            listeners: onItemClick
        ): ForgotPasswordDailog {
            this.listener = listeners
            return ForgotPasswordDailog(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog_Custom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        session = SessionManager(requireContext())
        autoDisposable.bindTo(this.lifecycle)
        _binding = DialogForgotPasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)

        _binding?.btnClose?.setOnClickListener {
          //  listener.onItemCLicked()
            dismissAllowingStateLoss()
        }
        _binding?.btnGetOtp?.setOnClickListener {
            listener.onItemCLicked()
            dismissAllowingStateLoss()
        }


    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    private fun populateData() {
        val bundle = arguments
        if (bundle != null) {
            val title = bundle.getString(Constant.TITLE)
            val text = bundle.getString(Constant.TEXT)
            //_binding?.txtTitle?.text = title
           // _binding?.tvText?.text = text
        }
    }

    interface onItemClick {
        fun onItemCLicked()
    }

    interface onDissmiss {
        fun onDismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }
}









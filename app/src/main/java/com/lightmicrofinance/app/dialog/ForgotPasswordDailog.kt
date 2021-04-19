package com.lightmicrofinance.app.dialog

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
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.databinding.DialogForgotPasswordBinding
import com.lightmicrofinance.app.extention.getValue
import com.lightmicrofinance.app.extention.isEmpty
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.extention.showSnackBar
import com.lightmicrofinance.app.modal.LoginModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.BlurDialogFragment
import com.lightmicrofinance.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ForgotPasswordDailog(context: Context) : BlurDialogFragment(), LifecycleOwner {

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
           validation()
        }


    }

    fun validation(){
        when {
           _binding?.edtPhone!!.isEmpty() -> {
                binding.mainView.showSnackBar(getString(R.string.enter_empid))
            }
            _binding?.edtPhone!!.getValue().length != 8 -> {
                binding.mainView.showSnackBar(getString(R.string.enter_valid_empid))
            }
            else -> {
                resetPassword()
            }


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
        fun onItemCLicked(password:String)
    }

    interface onDissmiss {
        fun onDismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding =null
    }


    fun resetPassword() {
        showProgressbar()
        val params = HashMap<String, Any>()
        params["FECode"] = _binding?.edtPhone!!.getValue()

        Networking
            .with(requireContext())
            .getServices()
            .resetPassword(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<LoginModal>() {
                override fun onSuccess(response: LoginModal) {
                    val data = response.data
                    hideProgressbar()
                    if (response.error == false) {
                        if (data != null) {
                            dismissAllowingStateLoss()
                            listener.onItemCLicked(data.password.toString())
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









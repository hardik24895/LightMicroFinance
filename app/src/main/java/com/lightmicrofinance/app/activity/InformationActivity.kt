package com.lightmicrofinance.app.activity

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.databinding.ActivityCmsBinding
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.modal.CMSDataModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class InformationActivity : BaseActivity() {
    lateinit var binding: ActivityCmsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCmsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val title = intent.getStringExtra(Constant.TITLE)
        if (title.equals("1")) {
            binding.txtTitle.text = "TERM AND CONDITION"
        } else if (title.equals("2")) {
            binding.txtTitle.text = "ABOUT US"
        } else {
            binding.txtTitle.text = "PRIVACY POLICY"
        }
        intent.getStringExtra("Desc")?.let { getCMSData(it) }
        binding.imgBack.setOnClickListener { onBackPressed() }

    }

    fun getCMSData(pageID: String) {

        showProgressbar()
        val params = HashMap<String, Any>()
        params["PageName"] = pageID

        Networking
            .with(this)
            .getServices()
            .getCMS(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CMSDataModal>() {
                override fun onSuccess(response: CMSDataModal) {
                    hideProgressbar()

                    if (response.error==false) {
                        val htmlStrig = response.data?.content

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            val htmlAsSpanned: Spanned =
                                Html.fromHtml(htmlStrig.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
                           binding.txtDesc.setText(htmlAsSpanned);
                        } else {
                            val htmlAsSpanned: Spanned = Html.fromHtml(htmlStrig.toString())
                            binding.txtDesc.setText(htmlAsSpanned);
                        }
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    showAlert(getString(R.string.show_server_error))
                }

            }).addTo(autoDisposable)
    }
}
package com.lightmicrofinance.commonproject.activity

import android.os.Bundle
import com.commonProject.utils.Constant
import com.lightmicrofinance.commonproject.databinding.ActivityCmsBinding


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
       // intent.getStringExtra("Desc")?.let { getCMSData(it) }
        binding.imgBack.setOnClickListener { onBackPressed() }

    }

   /* fun getCMSData(pageID: String) {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageName", pageID)

            result = Networking.setParentJsonData(
                Constant.METHOD_GET_PAGE,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .getCMS(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CMSModal>() {
                override fun onSuccess(response: CMSModal) {
                    hideProgressbar()

                    if (response.error == 200) {
                        val htmlStrig = response.data

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            val htmlAsSpanned: Spanned =
                                Html.fromHtml(htmlStrig, HtmlCompat.FROM_HTML_MODE_LEGACY)
                            txtDesc.setText(htmlAsSpanned);
                        } else {
                            val htmlAsSpanned: Spanned = Html.fromHtml(htmlStrig)
                            txtDesc.setText(htmlAsSpanned);
                        }
                    }
                }

                override fun onFailed(code: Int, message: String) {

                    hideProgressbar()

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                }

            }).addTo(autoDisposable)
    }*/
}
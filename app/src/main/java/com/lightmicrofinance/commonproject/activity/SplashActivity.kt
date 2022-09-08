package com.lightmicrofinance.commonproject.activity

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.commonProject.extention.goToActivityAndClearTask
import com.commonProject.utils.SessionManager
import com.lightmicrofinance.commonproject.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        Handler(Looper.getMainLooper()).postDelayed({
            validateRedirection()
        }, 1000)

       // binding.txtHello.text ="Got it"

    }
    private fun validateRedirection() {
        if (session.isLoggedIn) {
            goToActivityAndClearTask<MainActivity>()
        } else{
            goToActivityAndClearTask<LoginActivity>()
        }
    }

 /*   fun validation() {
        when {
            edtUsername.isEmpty() -> {
                root.showSnackBar("Enter Mobile Number")
                edtUsername.requestFocus()
            }
            edtUsername.getValue().length < 10 -> {
                root.showSnackBar("Enter Valid Mobile Number")
                edtUsername.requestFocus()
            }
            edtPassword.isEmpty() -> {
                root.showSnackBar("Enter Password")
                edtPassword.requestFocus()
            }
            edtPassword.getValue().length < 6 -> {
                edtPassword.requestFocus()
                root.showSnackBar("Enter Minimum six character")
            }

            else -> {
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    val token = task.result

                    login(token.toString())
                    // Log and toast
                    // val msg = getString(R.string.msg_token_fmt, token)
                    Log.d("token", token.toString())
                    //  Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                })

            }

        }
    }*/

  /*  fun login(fcmTokan: String?) {
        var result = ""
        showProgressbar()
        try {
            val jsonBody = JSONObject()
            jsonBody.put("EmailID", edtUsername.getValue())
            jsonBody.put("Password", edtPassword.getValue())
            jsonBody.put("DeviceUID", DeviceUtils.getDeviceId(this))
            jsonBody.put("DeviceName", DeviceUtils.getDeviceName())
            jsonBody.put("DeviceOS", DeviceUtils.getDeviceOS())
            jsonBody.put("OSVersion", DeviceUtils.getDeviceOSNumber())
            jsonBody.put("DeviceTokenID", fcmTokan)
            jsonBody.put("DeviceType", "Android")
            jsonBody.put("UserType", "Andriod")

            result = Networking.setParentJsonData(
                Constant.METHOD_LOGIN,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(this)
            .getServices()
            .login(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<LoginModal>() {
                override fun onSuccess(response: LoginModal) {
                    val data = response.data
                    hideProgressbar()
                    if (data != null) {
                        if (response.error == 200) {
                            session.user = response
                            getRole(data.roleID)
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
    }*/
}
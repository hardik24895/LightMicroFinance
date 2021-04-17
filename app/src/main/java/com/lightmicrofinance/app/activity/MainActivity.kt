package com.lightmicrofinance.app.activity

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.databinding.ActivityMainBinding
import com.lightmicrofinance.app.dialog.LogoutDailog
import com.lightmicrofinance.app.extention.goToActivityAndClearTask
import com.lightmicrofinance.app.extention.replaceFragment
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.fragment.*
import com.lightmicrofinance.app.modal.UserStatusModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import com.lightmicrofinance.app.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var toolbar1: Toolbar
    lateinit var txtTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        toolbar1 = binding.appbarMain.toolbar
        txtTitle = binding.appbarMain.tvTitle
        //  val includedView: ImageView = binding.contentMain.includes.imgBack
        setContentView(view)
        setSupportActionBar(binding.appbarMain.toolbar)

        binding.appbarMain.toolbar.setNavigationIcon(R.drawable.ic_menu)

        binding.appbarMain.toolbar.setNavigationOnClickListener { toggleLeftDrawer() }

        // includedView.setImageResource(R.drawable.ic_menu)
        //  includedView.setOnClickListener { toggleLeftDrawer() }

        drawerClickIteam()

        // binding.appbarMain.tvTitle.text = getString(R.string.home)
        replaceFragment(HomeFragment(), R.id.framLayout)

        try {
            val pInfo: PackageInfo = getPackageManager().getPackageInfo(getPackageName(), 0)
            val version = pInfo.versionName
            binding.leftDrawerMenu.txtVersion.text = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        if (session.user.data?.userType == Constant.FE)
            binding.leftDrawerMenu.txtName.text = session.user.data?.fEName
        else
            binding.leftDrawerMenu.txtName.text = session.user.data?.bMName


        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {

                if (session.user.data?.userType == Constant.FE)
                    binding.leftDrawerMenu.txtName.text = session.user.data?.fEName
                else
                    binding.leftDrawerMenu.txtName.text = session.user.data?.bMName


                /*  Glide.with(this@MainActivity)
                    .load(session.user.data?.photoURL)
                    .apply(
                        com.bumptech.glide.request.RequestOptions().centerCrop()
                            .placeholder(R.drawable.test_profile)
                    )
                    .into(binding.leftDrawerMenu.roundedImageView)*/
            }

            override fun onDrawerClosed(drawerView: View) {

            }

            override fun onDrawerStateChanged(newState: Int) {
                val inputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()?.getWindowToken(), 0)
            }
        })
    }

    fun drawerClickIteam() {

        binding.leftDrawerMenu.imgBack.setOnClickListener {
            toggleLeftDrawer()
        }

        binding.leftDrawerMenu.linHome.setOnClickListener {
            toggleLeftDrawer()
            //  binding.appbarMain.tvTitle.text = getString(R.string.home)
            replaceFragment(HomeFragment(), R.id.framLayout)
        }

        binding.leftDrawerMenu.linCollection.setOnClickListener {

            toolbar1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            toggleLeftDrawer()
            binding.appbarMain.tvTitle.text = getString(R.string.collection)
            replaceFragment(CollectionFragment(), R.id.framLayout)
        }

        binding.leftDrawerMenu.linPar.setOnClickListener {
            toggleLeftDrawer()
            toolbar1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            binding.appbarMain.tvTitle.text = getString(R.string.par)
            replaceFragment(ParFragment(), R.id.framLayout)
        }



        binding.leftDrawerMenu.linGoalSheet.setOnClickListener {
            toggleLeftDrawer()
            toolbar1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            binding.appbarMain.tvTitle.text = getString(R.string.goal_sheet)
            replaceFragment(GoalSheetFragment(), R.id.framLayout)
        }


        binding.leftDrawerMenu.linBusiness.setOnClickListener {
            toggleLeftDrawer()
            toolbar1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            binding.appbarMain.tvTitle.text = getString(R.string.bussiness)
            replaceFragment(BusinessFragment(), R.id.framLayout)
        }

        binding.leftDrawerMenu.linReport.setOnClickListener {
            toggleLeftDrawer()
            toolbar1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            binding.appbarMain.tvTitle.text = getString(R.string.report)
            replaceFragment(ReportFragment(), R.id.framLayout)
        }

        binding.leftDrawerMenu.linSetting.setOnClickListener {
            toolbar1.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            }
            toggleLeftDrawer()
            binding.appbarMain.tvTitle.text = getString(R.string.setting)
            replaceFragment(SettingFragment(), R.id.framLayout)
        }

        binding.leftDrawerMenu.linLogout.setOnClickListener {
            val dialog = LogoutDailog.newInstance(
                this,
                object : LogoutDailog.onItemClick {
                    override fun onItemCLicked() {

                        session.clearSession()
                        session.storeDataByKey(SessionManager.IsFirst, false)
                        goToActivityAndClearTask<LoginActivity>()
                    }
                })
            val bundle = Bundle()
            bundle.putString(Constant.TITLE, this.getString(R.string.app_name))
            bundle.putString(Constant.TEXT, this.getString(R.string.msg_logout))
            dialog.arguments = bundle
            dialog.show(supportFragmentManager, "YesNO")
        }


        /* binding.contentMain.includes.imgAdd.setOnClickListener {
             goToActivity<SearchActivty>()
         }*/


    }


    override fun onResume() {
        super.onResume()
        checkUserSatus()
    }

    private fun toggleLeftDrawer() {

        if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
        } else {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }


    }

    fun checkUserSatus() {
        val params = HashMap<String, Any>()
        params["FECode"] = session.user.data?.fECode.toString()
        params["BMCode"] = session.user.data?.bMCode.toString()

        Networking
            .with(this)
            .getServices()
            .checkUserStatus(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<UserStatusModal>() {
                override fun onSuccess(response: UserStatusModal) {
                    val data = response.data
                    if (response.error == false) {
                        if (data != null) {
                            if (data.status == "0") {
                                session.isLoggedIn = false
                                goToActivityAndClearTask<LoginActivity>()
                            }
                        } else {
                            showAlert(response.message.toString())
                        }
                    } else {
                        showAlert(response.message.toString())
                    }

                }

                override fun onFailed(code: Int, message: String) {
                    showAlert(message)
                }

            }).addTo(autoDisposable)
    }
}
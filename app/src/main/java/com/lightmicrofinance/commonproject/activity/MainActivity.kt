package com.lightmicrofinance.commonproject.activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide

import com.commonProject.extention.*
import com.commonProject.utils.Constant
import com.commonProject.utils.SessionManager
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.databinding.ActivityMainBinding
import com.lightmicrofinance.commonproject.dialog.LogoutDailog

import com.lightmicrofinance.commonproject.fragment.CollectionFragment
import com.lightmicrofinance.commonproject.fragment.HomeFragment
import com.lightmicrofinance.commonproject.fragment.ParFragment
import com.lightmicrofinance.commonproject.fragment.SettingFragment

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
      //  val includedView: ImageView = binding.contentMain.includes.imgBack
        setContentView(view)
        setSupportActionBar(binding.appbarMain.toolbar)

        binding.appbarMain.toolbar.setNavigationIcon(R.drawable.ic_menu)

        binding.appbarMain.toolbar.setNavigationOnClickListener { toggleLeftDrawer() }

       // includedView.setImageResource(R.drawable.ic_menu)
      //  includedView.setOnClickListener { toggleLeftDrawer() }

        drawerClickIteam()

        binding.appbarMain.tvTitle.text = getString(R.string.home)
        replaceFragment(HomeFragment(), R.id.framLayout)

        binding.leftDrawerMenu.txtName.text = session.user.data?.name


        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            }

            override fun onDrawerOpened(drawerView: View) {
                binding.leftDrawerMenu.txtName.setText(session.user.data?.name)


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
            binding.appbarMain.tvTitle.text = getString(R.string.home)
            replaceFragment(HomeFragment(), R.id.framLayout)
        }

       binding.leftDrawerMenu.linCollection.setOnClickListener {

           toggleLeftDrawer()
           binding.appbarMain.tvTitle.text = getString(R.string.collection)
           replaceFragment(CollectionFragment(), R.id.framLayout)
       }

        binding.leftDrawerMenu.linPar.setOnClickListener {
            toggleLeftDrawer()
            binding.appbarMain.tvTitle.text = getString(R.string.par)
            replaceFragment(ParFragment(), R.id.framLayout)
        }

        binding.leftDrawerMenu.linSetting.setOnClickListener {

            toggleLeftDrawer()
            binding.appbarMain.tvTitle.text =getString(R.string.setting)
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

    private fun toggleLeftDrawer() {

        if (binding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            binding.drawerLayout.closeDrawer(Gravity.LEFT)
        } else {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }


    }
}
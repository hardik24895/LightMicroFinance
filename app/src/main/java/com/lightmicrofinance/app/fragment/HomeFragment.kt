package com.lightmicrofinance.app.fragment


import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.activity.LoginActivity
import com.lightmicrofinance.app.activity.MainActivity
import com.lightmicrofinance.app.databinding.FragmentHomeBinding
import com.lightmicrofinance.app.databinding.RowHomeSliderBinding
import com.lightmicrofinance.app.extention.goToActivityAndClearTask
import com.lightmicrofinance.app.extention.showAlert
import com.lightmicrofinance.app.modal.CollectionSummaryDataItem
import com.lightmicrofinance.app.modal.CollectionSummaryModal
import com.lightmicrofinance.app.modal.ConfigDataModel
import com.lightmicrofinance.app.modal.UserStatusModal
import com.lightmicrofinance.app.network.CallbackObserver
import com.lightmicrofinance.app.network.Networking
import com.lightmicrofinance.app.network.addTo
import com.lightmicrofinance.app.utils.Constant
import com.lightmicrofinance.app.utils.SessionManager
import com.lightmicrofinance.app.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.HashMap


class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var adapter: HomeAdapter? = null
    private var list: MutableList<CollectionSummaryDataItem> = mutableListOf()
    var currentPosition: Int = 0
    var constrain: ConstraintLayout? = null
    var constrain6: ConstraintLayout? = null
    var txtDemand: TextView? = null
    var txtCollection: TextView? = null
    var txtPending: TextView? = null
    var txtCollectedPar: TextView? = null
    var txtCleint: TextView? = null
    lateinit var layoutManager: LinearLayoutManager
    var review_position: Int = 0
    var window: Window? = null
    /*companion object{
        var currentPosition: Int = 0
    }*/


    //var setSlider: SliderAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        // setupRecyclerView()
        // getCollectionSummary()
        checkUserSatus()


    }

    fun setupRecyclerView() {

        layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvHome.layoutManager = layoutManager
        val snapHelper: SnapHelper = PagerSnapHelper()
        //  binding.rvHome.setLayoutManager(layoutManager)
        snapHelper.attachToRecyclerView(binding.rvHome)
        adapter = HomeAdapter(requireContext(), list, session, "")
        binding.rvHome.adapter = adapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        getCollectionSummary()

        window = requireActivity().window


        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window?.setStatusBarColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }

        (mContext as MainActivity).txtTitle.text = requireActivity().getString(R.string.collected)
        (mContext as MainActivity).toolbar1.setBackgroundColor(
            requireActivity().resources.getColor(
                R.color.green
            )
        )

        binding.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    //Dragging
                } else if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    review_position = layoutManager.findFirstVisibleItemPosition();
                }

                var i: Int = layoutManager!!.findLastCompletelyVisibleItemPosition()

                //requireContext().showToast("" + i)
                if (i != -1)
                    setViewData(i)

            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val position: Int = layoutManager.findFirstVisibleItemPosition()


            }

        });

        /*   binding.rvHome.addOnScrollListener(object : RecyclerView.OnScrollListener() {

           })*/

        /*  binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
              override fun onPageScrolled(
                  position: Int,
                  positionOffset: Float,
                  positionOffsetPixels: Int
              ) {

              }

              override fun onPageSelected(position: Int) {
                  currentPosition = position
                  //updateBackground(position)

              }

              override fun onPageScrollStateChanged(state: Int) {

              }

          })
  */

        getConfig()
    }

    private fun setViewData(position: Int) {
        val window = (context as MainActivity).window
        if (position == 0) {


            binding.imgdot1.setImageResource(R.drawable.bg_selected_dot_collected)
            binding.imgdot2.setImageResource(R.drawable.bg_unselected_dot)
            binding.imgdot3.setImageResource(R.drawable.bg_unselected_dot)
            binding.imgdot4.setImageResource(R.drawable.bg_unselected_dot)

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            }

            (mContext as MainActivity).txtTitle.text =
                requireActivity().getString(R.string.collected)
            (mContext as MainActivity).toolbar1.setBackgroundColor(
                requireActivity().resources.getColor(
                    R.color.green
                )
            )


        } else if (position == 1) {

            binding.imgdot2.setImageResource(R.drawable.bg_selected_dot_partialy)
            binding.imgdot1.setImageResource(R.drawable.bg_unselected_dot)
            binding.imgdot3.setImageResource(R.drawable.bg_unselected_dot)
            binding.imgdot4.setImageResource(R.drawable.bg_unselected_dot)

            (mContext as MainActivity).txtTitle.text =
                requireActivity().getString(R.string.partialy)
            (mContext as MainActivity).toolbar1.setBackgroundColor(
                requireActivity().resources.getColor(
                    R.color.partialy_color
                )
            )

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.partialy_color
                    )
                )
            }


        } else if (position == 2) {

            binding.imgdot3.setImageResource(R.drawable.bg_selected_dot_pending)
            binding.imgdot2.setImageResource(R.drawable.bg_unselected_dot)
            binding.imgdot1.setImageResource(R.drawable.bg_unselected_dot)
            binding.imgdot4.setImageResource(R.drawable.bg_unselected_dot)

            (mContext as MainActivity).txtTitle.text =
                requireActivity().getString(R.string.pending)
            (mContext as MainActivity).toolbar1.setBackgroundColor(
                requireActivity().resources.getColor(
                    R.color.pending_color
                )
            )

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pending_color
                    )
                )
            }


        } else if (position == 3) {


            binding.imgdot4.setImageResource(R.drawable.bg_selected_dot_total)
            binding.imgdot2.setImageResource(R.drawable.bg_unselected_dot)
            binding.imgdot3.setImageResource(R.drawable.bg_unselected_dot)
            binding.imgdot1.setImageResource(R.drawable.bg_unselected_dot)

            (mContext as MainActivity).txtTitle.text =
                requireActivity().getString(R.string.g_total)
            (mContext as MainActivity).toolbar1.setBackgroundColor(
                requireActivity().resources.getColor(
                    R.color.colorPrimary
                )
            )

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
            }


        }
    }


    fun getCollectionSummary() {
        showProgressbar()
        list.clear()
        val params = HashMap<String, Any>()
        params["FECode"] = session.user.data?.fECode.toString()
        params["BMCode"] = session.user.data?.bMCode.toString()

        Networking
            .with(requireContext())
            .getServices()
            .getCollectionSummary(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CollectionSummaryModal>() {
                override fun onSuccess(response: CollectionSummaryModal) {
                    hideProgressbar()
                    if (response.error == false) {
                        list.addAll(response.data)
                        adapter?.notifyDataSetChanged()

                        /* binding.pager.adapter = setSlider
                        binding.wormDotsIndicator.setViewPager(binding.pager)
                        if (list.size > 0) {
                            //updateBackground(currentPosition)
                        }*/
                    }


                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    showAlert(getString(R.string.show_server_error))
                    //  refreshData(message, code)
                }

            }).addTo(autoDisposable)
    }


    inner class HomeAdapter(
        private val mContext: Context,
        var list: MutableList<CollectionSummaryDataItem> = mutableListOf(),
        var session: SessionManager,
        var status: String
    ) : RecyclerView.Adapter<HomeAdapter.ItemHolder>() {

        lateinit var binding: RowHomeSliderBinding

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            binding = RowHomeSliderBinding.inflate(
                LayoutInflater
                    .from(parent.getContext()), parent, false
            )
            return ItemHolder(
                binding
            )
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            val data = list[position]
            holder.bindData(mContext, data, session, status, position)
            constrain = binding.constrain
            constrain6 = binding.constrain6
            txtCollectedPar = binding.txtCollectedPar
            txtDemand = binding.txtDemand
            txtCollection = binding.txtCollection
            txtPending = binding.txtPending
            txtCleint = binding.txtCleint

        }


        inner class ItemHolder(containerView: RowHomeSliderBinding) :
            RecyclerView.ViewHolder(containerView.root) {
            var binding = containerView


            fun bindData(
                context: Context,
                data: CollectionSummaryDataItem,
                session: SessionManager,
                status: String,
                position: Int

            ) {


                binding.constrain.setBackgroundColor(Color.parseColor(data.color))
                binding.constrain6?.setBackgroundColor(Color.parseColor(data.color))
                /* (mContext as MainActivity).txtTitle.text = data.title
                 (mContext as MainActivity).toolbar1.setBackgroundColor(
                     Color.parseColor(data.color)
                 )

                 if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                     window?.setStatusBarColor(
                         Color.parseColor(data.color)
                     )
                 }*/

                binding.txtCollection?.text = "\u20b9 " + data.response?.collected
                binding.txtCollectedPar?.text = data.response?.percentage
                binding.txtDemand?.text = "\u20b9 " + data.response?.target
                binding.txtCleint?.text = data.response?.clients
                binding.txtPending?.text = data.response?.pending


            }
        }


    }
    /*fun updateBackground(position: Int){
        if (position == 0) {
            binding.wormDotsIndicator.setDotIndicatorColor(
                (requireActivity().resources.getColor(R.color.green))
            )
            binding.wormDotsIndicator.setStrokeDotsIndicatorColor(
                (requireActivity().resources.getColor(R.color.green))
            )

            constrain?.setBackgroundColor(requireActivity().resources.getColor(R.color.green))
            constrain6?.setBackgroundColor(requireActivity().resources.getColor(R.color.green))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.green
                    )
                )
            }
            (mContext as MainActivity).txtTitle.text = requireActivity().getString(R.string.collected)
            (mContext as MainActivity).toolbar1.setBackgroundColor(
                requireActivity().resources.getColor(
                    R.color.green
                )
            )

            txtCollection?.text = "\u20b9 " + list.get(position).collected?.collected
            txtCollectedPar?.text = list.get(position).collected?.percentage
            txtDemand?.text = "\u20b9 " + list.get(position).collected?.target
            txtClients?.text = list.get(position).collected?.clients
        } else if (position == 1) {
            binding.wormDotsIndicator.setDotIndicatorColor(
                (requireActivity().resources.getColor(
                    R.color.partialy_color
                ))
            )
            binding.wormDotsIndicator.setStrokeDotsIndicatorColor(
                (requireActivity().resources.getColor(
                    R.color.partialy_color
                ))
            )

            (mContext as MainActivity).txtTitle.text =
                requireActivity().getString(R.string.partialy)
            (mContext as MainActivity).toolbar1.setBackgroundColor(
                requireActivity().resources.getColor(
                    R.color.partialy_color
                )
            )

            constrain?.setBackgroundColor(requireActivity().resources.getColor(R.color.partialy_color))
            constrain6?.setBackgroundColor(requireActivity().resources.getColor(R.color.partialy_color))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.partialy_color
                    )
                )
            }

            txtCollection?.text = "\u20b9 " + list.get(position).partialy?.collected
            txtCollectedPar?.text = list.get(position).partialy?.percentage
            txtDemand?.text = "\u20b9 " + list.get(position).partialy?.target
            txtClients?.text = list.get(position).partialy?.clients
        } else if (position == 2) {
            binding.wormDotsIndicator.setDotIndicatorColor(
                (requireActivity().resources.getColor(
                    R.color.pending_color
                ))
            )
            binding.wormDotsIndicator.setStrokeDotsIndicatorColor(
                (requireActivity().resources.getColor(
                    R.color.pending_color
                ))
            )

            (mContext as MainActivity).txtTitle.text =
                requireActivity().getString(R.string.pending)
            (mContext as MainActivity).toolbar1.setBackgroundColor(
                requireActivity().resources.getColor(
                    R.color.pending_color
                )
            )

            constrain?.setBackgroundColor(requireActivity().resources.getColor(R.color.pending_color))
            constrain6?.setBackgroundColor(requireActivity().resources.getColor(R.color.pending_color))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.pending_color
                    )
                )
            }

            txtCollection?.text = "\u20b9 " + list.get(position).pending?.collected
            txtCollectedPar?.text = list.get(position).pending?.percentage
            txtDemand?.text = "\u20b9 " + list.get(position).pending?.target
            txtClients?.text = list.get(position).pending?.clients
        } else if (position == 3) {
            (mContext as MainActivity).txtTitle.text =
                requireActivity().getString(R.string.g_total)
            (mContext as MainActivity).toolbar1.setBackgroundColor(
                requireActivity().resources.getColor(
                    R.color.colorPrimary
                )
            )
            binding.wormDotsIndicator.setDotIndicatorColor(
                (requireActivity().resources.getColor(
                    R.color.colorPrimary
                ))
            )
            binding.wormDotsIndicator.setStrokeDotsIndicatorColor(
                (requireActivity().resources.getColor(
                    R.color.colorPrimary
                ))
            )
            constrain?.setBackgroundColor(requireActivity().resources.getColor(R.color.colorPrimary))
            constrain6?.setBackgroundColor(requireActivity().resources.getColor(R.color.colorPrimary))
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                window?.setStatusBarColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorPrimary
                    )
                )
            }

            txtCollection?.text = "\u20b9 " + list.get(position).all?.collected
            txtCollectedPar?.text = list.get(position).all?.percentage
            txtDemand?.text = "\u20b9 " + list.get(position).all?.target
            txtClients?.text = list.get(position).all?.clients
        }
    }*/

    /*  inner class SliderAdapter(
          private val mContext: Context,
          var list: MutableList<CollectionSummaryDataItem> = mutableListOf()
      ) :
          PagerAdapter() {
          private val inflater: LayoutInflater


          override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
              container.removeView(`object` as View)
          }

          override fun getCount(): Int {
              return list.size
          }

          override fun instantiateItem(view: ViewGroup, position: Int): Any {
              val row_layout: View = inflater.inflate(R.layout.row_home_slider, view, false)
              constrain = row_layout.findViewById(R.id.constrain)
              constrain6 = row_layout.findViewById(R.id.constrain6)
              txtDemand = row_layout.findViewById(R.id.txtDemand)
              txtPending = row_layout.findViewById(R.id.txtPending)
              txtCollection = row_layout.findViewById(R.id.txtCollection)
              txtCollectedPar = row_layout.findViewById(R.id.txtCollectedPar)
              txtClients = row_layout.findViewById(R.id.txtCleint)
              updateBackground(position)
              view.addView(row_layout)

              return row_layout
          }

          override fun isViewFromObject(view: View, `object`: Any): Boolean {
              return view == `object`
          }

          override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}
          override fun saveState(): Parcelable? {
              return null
          }

          override fun getItemPosition(`object`: Any): Int {
              return super.getItemPosition(`object`)
          }
          init {
              inflater = LayoutInflater.from(mContext)
          }
      }*/
    fun checkUserSatus() {
        val params = java.util.HashMap<String, Any>()
        params["FECode"] = session.user.data?.fECode.toString()
        params["BMCode"] = session.user.data?.bMCode.toString()

        Networking
            .with(requireContext())
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
                    showAlert(getString(R.string.show_server_error))
                }

            }).addTo(autoDisposable)
    }

    private fun checkCurrentVersion(CurrentAppVersion: Int): Boolean {
        var status = true
        if (CurrentAppVersion > 0) {
            if (Utils.getAppVersionCode(requireContext()) >= CurrentAppVersion) {
                status = true
            } else {
                status = false
                AlertDialog.Builder(mActivity)
                    .setTitle("")
                    .setMessage(getString(R.string.msg_install_latest_version))
                    .setCancelable(false)
                    .setPositiveButton(mActivity!!.getString(R.string.action_update),
                        DialogInterface.OnClickListener { dialog, whichButton ->
                            dialog.dismiss()
                            checkPermission()
                           // downloadFile()

                        })
                    .show()
            }
        }
        return status
    }

    fun getConfig() {
        val params = HashMap<String, Any>()
        /* params["FECode"] = binding.edtEmpId.getValue()
         params["Password"] = binding.edtPassword.getValue()*/

        Networking
            .with(requireContext())
            .getServices()
            .getConfig(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<ConfigDataModel>() {
                override fun onSuccess(response: ConfigDataModel) {
                    val data = response.data
                    if (response.error == false) {
                        if (data != null) {
                            session.configData = response
                            try {
                                val AppVersion: Int =
                                    session.configData.data?.appVersionAndroid?.toInt()!!
                                checkCurrentVersion(AppVersion)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
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

    fun downloadFile() {
        val Download_Uri = Uri.parse(Constant.APK_DOWNLOAD);
        val downloadManager =
            requireActivity().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val request = DownloadManager.Request(Download_Uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setTitle("LMF.apk")
        request.setDescription("NEW Version Downloading")
        request.setVisibleInDownloadsUi(true)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "LMF.apk"
        )

        val refid = downloadManager.enqueue(request)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        checkPermission()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        checkPermission()
    }
    fun checkPermission() {
        askPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) {
            //getLocation()
            val atualizaApp = UpdateApp()
            atualizaApp.setContext(requireActivity())
            atualizaApp.execute(Constant.APK_DOWNLOAD)
            //Request location updates:
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(requireContext()).setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .show();
            }

            if (e.hasForeverDenied()) {
                AlertDialog.Builder(requireContext()).setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.goToSettings()
                    } //ask again
                    .show();
            }
        }
    }

    inner class UpdateApp() : AsyncTask<String, Int, String>() {
        private var mPDialog: ProgressDialog? = null
        private var mContext: Context? = null
        fun setContext(context: Activity) {
            mContext = context
            context.runOnUiThread {
                mPDialog = ProgressDialog(mContext)
                mPDialog?.setMessage("Please wait...")
                mPDialog?.setIndeterminate(true)
                mPDialog?.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
                mPDialog?.setCancelable(false)
                mPDialog?.show()
            }
        }

        override fun doInBackground(vararg arg0: String): String? {
            try {
                val url = URL(arg0[0])
                val c: HttpURLConnection = url.openConnection() as HttpURLConnection
                c.setRequestMethod("GET")
                c.setDoOutput(true)
                c.connect()
                val lenghtOfFile: Int = c.getContentLength()
                val PATH: String =
                    Objects.requireNonNull(mContext!!.getExternalFilesDir(null))!!.getAbsolutePath()
                val file = File(PATH)
                val isCreate: Boolean = file.mkdirs()
                val outputFile = File(file, "LMF.apk")
                if (outputFile.exists()) {
                    val isDelete: Boolean = outputFile.delete()
                }
                val fos = FileOutputStream(outputFile)
                val `is`: InputStream = c.getInputStream()
                val buffer = ByteArray(1024)
                var len1: Int
                var total: Long = 0
                while (`is`.read(buffer).also { len1 = it } != -1) {
                    total += len1.toLong()
                    fos.write(buffer, 0, len1)
                    publishProgress((total * 100 / lenghtOfFile).toInt())
                }
                fos.close()
                `is`.close()
                if (mPDialog != null) mPDialog?.dismiss()
                installApk()
            } catch (e: java.lang.Exception) {
                Log.e("UpdateAPP", "Update error! " + e.message)
            }
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
            if (mPDialog != null) mPDialog?.show()
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            if (mPDialog != null) {
                mPDialog?.setIndeterminate(false)
                mPDialog?.setMax(100)
                values[0]?.toInt()?.let { mPDialog?.setProgress(it) }
            }
        }

        override fun onPostExecute(result: String?) {
            if (mPDialog != null) mPDialog?.dismiss()
            if (result != null) Toast.makeText(
                mContext,
                "Download error: $result",
                Toast.LENGTH_LONG
            ).show() else Toast.makeText(mContext, "File Downloaded", Toast.LENGTH_SHORT).show()
        }

        private fun installApk() {
            try {
                val PATH: String =
                    Objects.requireNonNull(mContext!!.getExternalFilesDir(null))!!.getAbsolutePath()
                val file = File("$PATH/LMF.apk")
                val intent = Intent(Intent.ACTION_VIEW)
                if (Build.VERSION.SDK_INT >= 24) {
                    val downloaded_apk = FileProvider.getUriForFile(
                        mContext!!,
                        mContext!!.applicationContext.packageName + ".provider",
                        file
                    )
                    intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive")
                    val resInfoList = mContext!!.packageManager.queryIntentActivities(
                        intent,
                        PackageManager.MATCH_DEFAULT_ONLY
                    )
                    for (resolveInfo in resInfoList) {
                        mContext!!.grantUriPermission(
                            mContext!!.applicationContext.packageName + ".provider",
                            downloaded_apk,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    }
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
                    startActivity(intent)
                } else {
                    intent.action = Intent.ACTION_VIEW
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                    intent.setDataAndType(
                        Uri.fromFile(file),
                        "application/vnd.android.package-archive"
                    )
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}
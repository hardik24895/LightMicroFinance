package com.lightmicrofinance.app.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.commonProject.interfaces.SnackbarActionListener
import com.commonProject.ui.dialog.ProgressDialog
import com.google.android.material.snackbar.Snackbar
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.extention.dismissAlertDialog
import com.lightmicrofinance.app.network.AutoDisposable
import com.ms_square.etsyblur.Blur
import com.ms_square.etsyblur.BlurConfig
import com.ms_square.etsyblur.ViewUtil

abstract class BlurDialogFragment : DialogFragment() {
    private var blur: Blur? = null
    private var root: ViewGroup? = null
    private var blurImgView: ImageView? = null
    var mContext: Context? = null
    var mActivity: Activity? = null
    private var lastClickTime: Long = 0
    lateinit var session: SessionManager
    var progressDialog: ProgressDialog? = null
    val autoDisposable = AutoDisposable()
    var snackbar: Snackbar? = null
    private val preDrawListener: ViewTreeObserver.OnPreDrawListener =
        object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                root!!.viewTreeObserver.removeOnPreDrawListener(this)
                // makes sure to get the complete drawing after the layout pass
                root!!.post {
                    setUpBlurringViews()
                    startEnterAnimation()
                }
                return true
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        blur = Blur(context, blurConfig())

        mContext = context
        mActivity = activity
        session = SessionManager(context)
        autoDisposable.bindTo(this.lifecycle)

        if (context is Activity) {
            root = context.window.decorView as ViewGroup
            if (root!!.isShown) {
                setUpBlurringViews()
                startEnterAnimation()
            } else {
                root!!.viewTreeObserver.addOnPreDrawListener(preDrawListener)
            }
        } else {
            Log.w(
                TAG,
                "onAttach(Context context) - context is not type of Activity. Currently Not supported."
            )
        }
    }

    override fun onStart() {
        val dialog = dialog
        if (dialog != null) {
            if (!backgroundDimmingEnabled()) {
                dialog.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
        }
        super.onStart()
    }

    override fun onDismiss(dialog: DialogInterface) {
        startExitAnimation()
        super.onDismiss(dialog)
    }

    override fun onDetach() {
        root!!.viewTreeObserver.removeOnPreDrawListener(preDrawListener)
        blur!!.destroy()
        super.onDetach()
    }

    /**
     * Configuration object for the blur effect.
     * If not overwritten, it just returns [BlurConfig.DEFAULT_CONFIG] which uses [com.ms_square.etsyblur.SimpleAsyncPolicy].
     *
     * @return blur operation configuration
     */
    protected fun blurConfig(): BlurConfig {
        return BlurConfig.DEFAULT_CONFIG
    }

    /**
     * Controls if everything behind this window will be dimmed.
     *
     * @return true if dimming should be enabled
     */
    protected fun backgroundDimmingEnabled(): Boolean {
        return DEFAULT_BACKGROUND_DIMMING_ENABLED
    }

    /**
     * Alpha animation duration (ms) of the blurred image added in this fragment's hosting activity.
     *
     * @return animation duration in ms
     */
    protected fun animDuration(): Int {
        return DEFAULT_ANIM_DURATION
    }

    private fun setUpBlurringViews() {
        val visibleFrame = Rect()
        root!!.getWindowVisibleDisplayFrame(visibleFrame)
        val params = FrameLayout.LayoutParams(
            visibleFrame.right - visibleFrame.left,
            visibleFrame.bottom - visibleFrame.top
        )
        params.setMargins(visibleFrame.left, visibleFrame.top, 0, 0)
        blurImgView = ImageView(root!!.context)
        blurImgView!!.layoutParams = params
        blurImgView!!.alpha = 0f
        root!!.addView(blurImgView)

        // apply blur effect
        val bitmapToBlur = ViewUtil.drawViewToBitmap(
            root,
            visibleFrame.right,
            visibleFrame.bottom,
            visibleFrame.left.toFloat(),
            visibleFrame.top.toFloat(),
            blurConfig().downScaleFactor(),
            blurConfig().overlayColor()
        )
        blur!!.execute(bitmapToBlur, true) { blurredBitmap ->
            blurImgView!!.setImageBitmap(
                blurredBitmap
            )
        }
    }

    private fun startEnterAnimation() {
        val monitor: Runnable = object : Runnable{
            override fun run() {
            }
        }


        if (blurImgView != null) {
            ViewUtil.animateAlpha(blurImgView!!, 0f, 1f, animDuration(), monitor)
        }
    }

    private fun startExitAnimation() {
        if (blurImgView != null) {
            ViewUtil.animateAlpha(blurImgView!!, 1f, 0f, animDuration()) {
                root!!.removeView(
                    blurImgView
                )
            }
        }
    }

    companion object {
        const val DEFAULT_ANIM_DURATION = 400
        const val DEFAULT_BACKGROUND_DIMMING_ENABLED = true
        private val TAG = BlurDialogFragment::class.java.simpleName
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    fun showProgressbar() {
        showProgressbar(null)
    }

    fun showProgressbar(message: String? = getString(R.string.please_wait)) {
        hideProgressbar()
        progressDialog = ProgressDialog(mContext!!, message)
        progressDialog?.show()
    }

    fun hideProgressbar() {
        if (progressDialog != null && progressDialog?.isShowing!!) progressDialog!!.dismiss()
    }

    fun showSoftKeyboard(view: EditText) {
        view.requestFocus(view.text.length)
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideSoftKeyboard(): Boolean {
        try {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        } catch (e: Exception) {
            return false
        }
    }

    /* fun setupToolbar(view: View, title: String? = null) {
         (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))
         val actionBar = (activity as AppCompatActivity).supportActionBar
         if (actionBar != null) {
             actionBar.setDisplayShowTitleEnabled(false)
             actionBar.setDisplayHomeAsUpEnabled(false)
             if (title != null) tvTitle.text = title
         }
     }

     fun setupToolBarWithMenu(view: View, title: String, icon: Int = R.drawable.v_ic_menu) {
         (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))
         val actionBar = (activity as AppCompatActivity).supportActionBar
         if (actionBar != null) {
             actionBar.setDisplayShowTitleEnabled(false)
             actionBar.setDisplayHomeAsUpEnabled(true)
             actionBar.setHomeAsUpIndicator(icon)
             tvTitle.text = title
         }
     }*/

    fun showSnackbar(view: View?, msg: String, LENGTH: Int) {
        if (view == null) return
        snackbar = Snackbar.make(view, msg, LENGTH)
        val sbView = snackbar?.view
        val textView =
            sbView?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView?.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
        snackbar?.show()
    }

    fun showSnackbar(
        view: View?,
        msg: String,
        LENGTH: Int,
        action: String,
        actionListener: SnackbarActionListener?
    ) {
        if (view == null) return
        snackbar = Snackbar.make(view, msg, LENGTH)
        snackbar?.setActionTextColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
        if (actionListener != null) {
            snackbar?.setAction(action) { view1 ->
                snackbar?.dismiss()
                actionListener.onAction()
            }
        }
        val sbView = snackbar?.view
        val textView =
            sbView?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView?.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
        snackbar?.show()
    }

    fun preventDoubleClick(view: View) {
        // preventing double, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
    }

    override fun onDestroy() {
        snackbar?.dismiss()
        dismissAlertDialog()
        hideProgressbar()
        super.onDestroy()
    }

}
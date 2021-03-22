package com.lightmicrofinance.commonproject.fragment


import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.commonProject.extention.showAlert
import com.commonProject.network.CallbackObserver
import com.commonProject.network.Networking
import com.commonProject.network.addTo
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.activity.MainActivity
import com.lightmicrofinance.commonproject.databinding.FragmentHomeBinding
import com.lightmicrofinance.commonproject.modal.CollectionSummaryDataItem
import com.lightmicrofinance.commonproject.modal.CollectionSummaryModal
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private var list: MutableList<CollectionSummaryDataItem> = mutableListOf()
    var currentPosition: Int = 0
    var constrain: ConstraintLayout? = null
    var constrain6: ConstraintLayout? = null
    var txtDemand: TextView? = null
    var txtCollection: TextView? = null
    var txtPending: TextView? = null
    var txtCollectedPar: TextView? = null
    var txtClients: TextView? = null
    var window: Window? = null
    /*companion object{
        var currentPosition: Int = 0
    }*/


    var setSlider: SliderAdapter? = null
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
        setSlider = SliderAdapter(requireContext(), list)
        getCollectionSummary()
        (mContext as MainActivity).txtTitle.text = requireActivity().getString(R.string.collected)
        (mContext as MainActivity).toolbar1.setBackgroundColor(
            requireActivity().resources.getColor(
                R.color.green
            )
        )

        binding.wormDotsIndicator.setDotIndicatorColor((requireActivity().resources.getColor(R.color.green)))
        binding.wormDotsIndicator.setStrokeDotsIndicatorColor(
            (requireActivity().resources.getColor(
                R.color.green
            ))
        )
        constrain?.setBackgroundColor(requireActivity().resources.getColor(R.color.green))
        constrain6?.setBackgroundColor(requireActivity().resources.getColor(R.color.green))
        window = requireActivity().window


        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            window?.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                if (position == 0) {
                    binding.wormDotsIndicator.setDotIndicatorColor(
                        (requireActivity().resources.getColor(
                            R.color.green
                        ))
                    )
                    binding.wormDotsIndicator.setStrokeDotsIndicatorColor(
                        (requireActivity().resources.getColor(
                            R.color.green
                        ))
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
                    (mContext as MainActivity).txtTitle.text =
                        requireActivity().getString(R.string.collected)
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


                // setSlider?.changepos(position)

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })


    }


    fun getCollectionSummary() {
        list.clear()
        val params = HashMap<String, Any>()
        params["FECode"] = session.user.data?.fECode.toString()

        Networking
            .with(requireContext())
            .getServices()
            .getCollectionSummary(Networking.wrapParams(params))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CollectionSummaryModal>() {
                override fun onSuccess(response: CollectionSummaryModal) {

                    if (response.error == false) {
                        list.addAll(response.data)
                        binding.pager.adapter = setSlider
                        binding.wormDotsIndicator.setViewPager(binding.pager)
                        if (list.size > 0) {
                            txtCollection?.text = "\u20b9 " + list.get(0).collected?.collected
                            txtCollectedPar?.text = list.get(0).collected?.percentage
                            txtDemand?.text = "\u20b9 " + list.get(0).collected?.target
                            txtClients?.text = list.get(0).collected?.clients
                        }
                    }


                }

                override fun onFailed(code: Int, message: String) {
                    showAlert(getString(R.string.show_server_error))
                    //  refreshData(message, code)
                }

            }).addTo(autoDisposable)
    }

    inner class SliderAdapter(
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
            if (currentPosition == 0) {
                txtCollection?.text = "\u20b9 " + list.get(0).collected?.collected
                txtCollectedPar?.text = list.get(0).collected?.percentage
                txtDemand?.text = "\u20b9 " + list.get(0).collected?.target
                txtClients?.text = list.get(0).collected?.clients

            }
            //  imageView.setImageResource(IMAGES.get(position));
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


        init {
            inflater = LayoutInflater.from(mContext)
        }
    }

}
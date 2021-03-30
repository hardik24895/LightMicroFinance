package com.lightmicrofinance.app.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.modal.CollectionSummaryDataItem

class SliderAdapter(
    private val mContext: Context,
    var list: MutableList<CollectionSummaryDataItem> = mutableListOf(),
    var currentPos: Int
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
        val constrain: ConstraintLayout = row_layout.findViewById(R.id.constrain)
        val constrain6: ConstraintLayout = row_layout.findViewById(R.id.constrain6)
        val txtDemand: TextView = row_layout.findViewById(R.id.txtDemand)


        /*   if (currentPos == 0) {
               (mContext as MainActivity).txtTitle.text = mContext.getString(R.string.collected)
               (mContext as MainActivity).toolbar1.setBackgroundColor(mContext.resources.getColor(R.color.colorPrimary))
               constrain.setBackgroundColor(mContext.resources.getColor(R.color.colorPrimary))
               constrain6.setBackgroundColor(mContext.resources.getColor(R.color.colorPrimary))

           } else if (currentPos == 1) {
               (mContext as MainActivity).txtTitle.text = mContext.getString(R.string.partialy)
               constrain.setBackgroundColor(mContext.resources.getColor(R.color.partialy_color))
               constrain6.setBackgroundColor(mContext.resources.getColor(R.color.partialy_color))
               (mContext as MainActivity).toolbar1.setBackgroundColor(mContext.resources.getColor(R.color.partialy_color))
           } else if (currentPos == 2) {
               (mContext as MainActivity).txtTitle.text = mContext.getString(R.string.pending)
               constrain.setBackgroundColor(mContext.resources.getColor(R.color.pending_color))
               constrain6.setBackgroundColor(mContext.resources.getColor(R.color.pending_color))
               (mContext as MainActivity).toolbar1.setBackgroundColor(mContext.resources.getColor(R.color.pending_color))
           } else if (currentPos == 3) {
               (mContext as MainActivity).txtTitle.text = mContext.getString(R.string.g_total)
               constrain.setBackgroundColor(mContext.resources.getColor(R.color.green))
               constrain6.setBackgroundColor(mContext.resources.getColor(R.color.green))
               (mContext as MainActivity).toolbar1.setBackgroundColor(mContext.resources.getColor(R.color.green))
           }*/
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

    fun changepos(pos: Int) {
        currentPos = pos
        notifyDataSetChanged()
    }

    init {
        inflater = LayoutInflater.from(mContext)
    }
}
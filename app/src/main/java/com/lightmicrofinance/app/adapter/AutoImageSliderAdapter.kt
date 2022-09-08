package com.lightmicrofinance.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.activity.MainActivity
import com.lightmicrofinance.app.databinding.RowHomeSliderBinding
import com.lightmicrofinance.app.modal.CollectionSummaryDataItem
import com.smarteist.autoimageslider.SliderViewAdapter


class AutoImageSliderAdapter(
    private val mContext: Context,
    var list: MutableList<CollectionSummaryDataItem> = mutableListOf(),
    private val listener: OnItemSelected
) : SliderViewAdapter<AutoImageSliderAdapter.AutoImageSliderItemHolder>() {

    lateinit var binding: RowHomeSliderBinding


    override fun getCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup): AutoImageSliderItemHolder {
        binding = RowHomeSliderBinding.inflate(
            LayoutInflater
                .from(parent.getContext()), parent, false
        )
        return AutoImageSliderItemHolder(
            binding
        )
    }


    override fun onBindViewHolder(holder: AutoImageSliderItemHolder, position: Int) {
        val data = list[position]
        holder.bindData(mContext, data, listener, position)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: CollectionSummaryDataItem)
    }

    class AutoImageSliderItemHolder(containerView: RowHomeSliderBinding) :
        ViewHolder(containerView.root) {
        val binding = containerView

        fun bindData(
            context: Context,
            data: CollectionSummaryDataItem,
            listener: OnItemSelected,
            position: Int
        ) {

            if (position == 0) {
                (context as MainActivity).txtTitle.text = context.getString(R.string.collected)
                (context as MainActivity).toolbar1.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                binding.constrain.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
                binding.constrain6.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))

            } else if (position == 1) {
                (context as MainActivity).txtTitle.text = context.getString(R.string.partialy)
                binding.constrain.setBackgroundColor(context.resources.getColor(R.color.partialy_color))
                binding.constrain6.setBackgroundColor(context.resources.getColor(R.color.partialy_color))
                (context as MainActivity).toolbar1.setBackgroundColor(context.resources.getColor(R.color.partialy_color))
            } else if (position == 2) {
                (context as MainActivity).txtTitle.text = context.getString(R.string.pending)
                binding.constrain.setBackgroundColor(context.resources.getColor(R.color.pending_color))
                binding.constrain6.setBackgroundColor(context.resources.getColor(R.color.pending_color))
                (context as MainActivity).toolbar1.setBackgroundColor(context.resources.getColor(R.color.pending_color))
            } else if (position == 3) {
                (context as MainActivity).txtTitle.text = context.getString(R.string.g_total)
                binding.constrain.setBackgroundColor(context.resources.getColor(R.color.green))
                binding.constrain6.setBackgroundColor(context.resources.getColor(R.color.green))
                (context as MainActivity).toolbar1.setBackgroundColor(context.resources.getColor(R.color.green))
            }

            /* txtCounterTitle.text = data.title

            var df = DecimalFormat("##.##")

            txtCounter.text = df.format(data.count?.toBigDecimal())
            txtCounter1.text = df.format(data.count?.toBigDecimal())

            if (position == 0) {
                main_view.setBackgroundColor(Color.parseColor("#d32f2f"))
            } else if (position == 1) {
                main_view.setBackgroundColor(Color.parseColor("#7b1fa2"))
            } else if (position == 2) {
                main_view.setBackgroundColor(Color.parseColor("#303f9f"))
            } else if (position == 3) {
                main_view.setBackgroundColor(Color.parseColor("#0288d1"))*/


        }
    }

}
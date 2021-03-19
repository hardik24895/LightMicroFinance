package com.lightmicrofinance.commonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


import com.commonProject.utils.SessionManager
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.databinding.RowBusinessBinding
import com.lightmicrofinance.commonproject.databinding.RowCollectionBinding
import com.lightmicrofinance.commonproject.databinding.RowParBinding
import com.lightmicrofinance.commonproject.modal.ParDataItem


class BusinessAdapter(
    private val mContext: Context,
    var list: MutableList<ParDataItem> = mutableListOf(),
    var session: SessionManager,
    var status: String,
    private val listener: BusinessAdapter.OnItemSelected,
) : RecyclerView.Adapter<BusinessAdapter.ItemHolder>() {

    lateinit var binding: RowBusinessBinding

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        binding = RowBusinessBinding.inflate(
            LayoutInflater
                .from(parent.getContext()), parent, false
        )
        return ItemHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]
        holder.bindData(mContext, data, listener, session, binding, status)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: ParDataItem, action: String)
    }

    class ItemHolder(containerView: RowBusinessBinding) :
        RecyclerView.ViewHolder(containerView.root) {
        fun bindData(
            context: Context,
            data: ParDataItem,
            listener: OnItemSelected, session: SessionManager,
            binding: RowBusinessBinding,
            status: String
        ) {


            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data, "MainView") }

        }
    }


}
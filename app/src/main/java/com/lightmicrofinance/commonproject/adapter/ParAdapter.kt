package com.lightmicrofinance.commonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


import com.commonProject.utils.SessionManager
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.databinding.RowCollectionBinding
import com.lightmicrofinance.commonproject.databinding.RowParBinding


class ParAdapter(
    private val mContext: Context,
    var list: MutableList<String> = mutableListOf(),
    var session: SessionManager,
    var status: String,
    private val listener: ParAdapter.OnItemSelected,
) : RecyclerView.Adapter<ParAdapter.ItemHolder>() {

    lateinit var binding: RowParBinding

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        binding = RowParBinding.inflate(
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
        fun onItemSelect(position: Int, data: String, action: String)
    }

    class ItemHolder(containerView: RowParBinding) :
        RecyclerView.ViewHolder(containerView.root) {
        fun bindData(
            context: Context,
            data: String,
            listener: OnItemSelected, session: SessionManager,
            binding: RowParBinding,
            status: String
        ) {

            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data, "MainView") }

        }
    }


}
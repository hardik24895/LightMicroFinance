package com.lightmicrofinance.commonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lightmicrofinance.commonproject.databinding.RowTicketBinding

import com.commonProject.utils.SessionManager


class LeadAdapter(
    private val mContext: Context,
    var list: MutableList<String> = mutableListOf(),
    var session: SessionManager,
    private val listener: LeadAdapter.OnItemSelected,
) : RecyclerView.Adapter<LeadAdapter.ItemHolder>() {

    lateinit var binding: RowTicketBinding

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        binding =   RowTicketBinding.inflate(
            LayoutInflater
                .from(parent.getContext()), parent, false
        )
        return ItemHolder(
           binding
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]
        holder.bindData(mContext, data, listener, session, binding)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: String, action: String)
    }

    class ItemHolder( containerView:  RowTicketBinding) :
        RecyclerView.ViewHolder(containerView.root) {
        fun bindData(
            context: Context,
            data: String,
            listener: OnItemSelected, session: SessionManager,
            binding: RowTicketBinding
        ) {

            //binding.imgProfile


            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data, "MainView") }

        }
    }


}
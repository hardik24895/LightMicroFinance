package com.lightmicrofinance.commonproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.commonProject.utils.Constant


import com.commonProject.utils.SessionManager
import com.lightmicrofinance.commonproject.R
import com.lightmicrofinance.commonproject.databinding.RowCollectionBinding
import com.lightmicrofinance.commonproject.modal.CollectionDataItem


class CollectionAdapter(
    private val mContext: Context,
    var list: MutableList<CollectionDataItem> = mutableListOf(),
    var session: SessionManager,
    var status: String,
    private val listener: CollectionAdapter.OnItemSelected,
) : RecyclerView.Adapter<CollectionAdapter.ItemHolder>() {

    lateinit var binding: RowCollectionBinding

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        binding =   RowCollectionBinding.inflate(
            LayoutInflater
                .from(parent.getContext()), parent, false
        )
        return ItemHolder(
           binding
        )
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val data = list[position]
        holder.bindData(mContext, data, listener, session, status)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: CollectionDataItem, action: String)
    }

    class ItemHolder(containerView: RowCollectionBinding) :
        RecyclerView.ViewHolder(containerView.root) {
        var binding = containerView
        fun bindData(
            context: Context,
            data: CollectionDataItem,
            listener: OnItemSelected, session: SessionManager,
            status: String
        ) {


            if (status == Constant.PENDING) {
                binding.imgCardBg.setImageResource(R.drawable.orange_card)
            }else if (status==Constant.PARTIALY){
                binding.imgCardBg.setImageResource(R.drawable.blue_card)
            }else{
                binding.imgCardBg.setImageResource(R.drawable.green_card)
            }

            binding.txtCleintID.text = data.clientID
            binding.txtLoanID.text = data.loanID
            binding.txtCenterName.text = data.centerName
            binding.txtDate.text = data.dueDate
            binding.txtBranchName.text = data.branch
            binding.txtClientName.text = data.clientName
            binding.txtAmount.text = "₹ "+ data.originalDemand

            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data, "MainView") }

        }
    }


}
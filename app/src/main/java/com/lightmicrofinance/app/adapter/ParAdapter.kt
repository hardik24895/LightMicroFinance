package com.lightmicrofinance.app.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lightmicrofinance.app.databinding.RowParBinding
import com.lightmicrofinance.app.modal.ParDataItem
import com.lightmicrofinance.app.utils.SessionManager


class ParAdapter(
    private val mContext: Context,
    var list: MutableList<ParDataItem> = mutableListOf(),
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
        holder.bindData(mContext, data, listener, session)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: ParDataItem, action: String)
    }

    class ItemHolder(containerView: RowParBinding) : RecyclerView.ViewHolder(containerView.root) {
        val binding = containerView

        fun bindData(
            context: Context,
            data: ParDataItem,
            listener: OnItemSelected, session: SessionManager
        ) {

            binding.txtFeCodeNo.text = data.fECode
            binding.txtLoanID.text = data.loanID
            binding.txtBranchName.text = data.branch
            binding.txtDPD.text = data.dPD
            binding.txtCenterName.text = data.centerName
            binding.txtBucket.text = data.bucket
            binding.txtClientName.text = data.clientName
            binding.txtCleintID.text = data.clientID
            binding.txtAmount.text = "₹ " + data.amount
            binding.txtOverDue.text = "₹ " + data.overdueAmount


            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data, "MainView") }

        }
    }


}
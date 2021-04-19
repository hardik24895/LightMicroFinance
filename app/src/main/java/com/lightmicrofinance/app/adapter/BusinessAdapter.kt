package com.lightmicrofinance.app.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lightmicrofinance.app.databinding.RowBusinessBinding
import com.lightmicrofinance.app.modal.BusinessListDataItem
import com.lightmicrofinance.app.utils.SessionManager


class BusinessAdapter(
    private val mContext: Context,
    var list: MutableList<BusinessListDataItem> = mutableListOf(),
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
        holder.bindData(mContext, data, listener, session)
    }

    interface OnItemSelected {
        fun onItemSelect(position: Int, data: BusinessListDataItem, action: String)
    }

    class ItemHolder(containerView: RowBusinessBinding) :
        RecyclerView.ViewHolder(containerView.root) {
        val binding = containerView
        fun bindData(
            context: Context,
            data: BusinessListDataItem,
            listener: OnItemSelected, session: SessionManager
        ) {

            binding.txtDate.text = data.date
            binding.txtBranchName.text = data.branch
            binding.txLeNewTarget.text = data.lENew
            binding.txtLeNewAchived.text = data.achLENew
            binding.txtLeReNewAchived.text = data.achLERenew
            binding.txLeReNewTarget.text = data.lERenew
            binding.txTotalLETarget.text = data.totalLE
            binding.txtTotalLEAchived.text = data.achTotalLE
            binding.txDDDoneTarget.text = data.dDDone
            binding.txtDDDOneAchived.text = data.achDDDone
            binding.txtDDPosAchived.text = data.achDDPositive
            binding.txDDPosTarget.text = data.dDPositive
            binding.txtGRTAchived.text = data.achGRT
            binding.txGRTTarget.text = data.gRT
            binding.txtDSBAchived.text = data.achDisbClient
            binding.txDSBTarget.text = data.disbClient
            binding.txDSBAmountTarget.text = data.disbAmount
            binding.txtDSBAmountAchived.text = data.achDisbAmount



            itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data, "MainView") }

        }
    }


}
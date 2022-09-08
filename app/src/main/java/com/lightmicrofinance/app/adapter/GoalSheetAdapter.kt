package com.lightmicrofinance.app.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lightmicrofinance.app.R
import com.lightmicrofinance.app.databinding.RowGoalsheetBinding
import com.lightmicrofinance.app.modal.BusinessListDataItem
import com.lightmicrofinance.app.modal.GoalsheetDataItem
import com.lightmicrofinance.app.utils.SessionManager


class GoalSheetAdapter(
    private val mContext: Context,
    var list: MutableList<GoalsheetDataItem> = mutableListOf(),
    var session: SessionManager,
    var status: String,
    private val listener: GoalSheetAdapter.OnItemSelected,
) : RecyclerView.Adapter<GoalSheetAdapter.ItemHolder>() {

    lateinit var binding: RowGoalsheetBinding

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        binding = RowGoalsheetBinding.inflate(
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

    class ItemHolder(containerView: RowGoalsheetBinding) :
        RecyclerView.ViewHolder(containerView.root) {
        val binding = containerView
        fun bindData(
            context: Context,
            data: GoalsheetDataItem,
            listener: OnItemSelected, session: SessionManager
        ) {

            if (data.isCurrentMonth == "True") {
                binding.imgCardBg.setImageResource(R.drawable.green_bg)
            } else {
                binding.imgCardBg.setImageResource(R.drawable.bussiness_bg)
            }


            binding.txtEmployeerCode.text = data.fECode
            binding.txtBranchName.text = data.branch
            binding.txtMonth.text = data.month
            binding.txtOpeningCleintsNewTarget.text = data.openingClients
            binding.txtNewCleintsTarget.text = data.newClients
            binding.txtcloseCleintsTarget.text = data.closedClients
            binding.txtClosingCleintsTarget.text = data.closingClient
            binding.txtrenewDsTarget.text = data.renewDis
            binding.txtCollectionTarget.text = data.collectionPerc
            binding.txtachNewClients.text = data.achNewClients
            binding.txDSBTarget.text = data.totalDis
            binding.txtachOpeningClients.text = data.achOpeningClients
            //     binding.txtLeReNewAchived.text = data.ach
            binding.txtachClosedClients.text = data.achClosedClients
            binding.txtachRenewDis.text = data.achRenewDis
            binding.txtachClosingCliens.text = data.achClosingClient
            binding.txtachCollectionpr.text = data.achCollectionPerc
            binding.txtachTotalDis.text = data.achTotalDis


            // itemView.setOnClickListener { listener.onItemSelect(adapterPosition, data, "MainView") }

        }
    }


}
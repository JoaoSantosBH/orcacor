package com.jomar.senhorpintor.ui.historic.adapter

import android.animation.AnimatorInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.model.entities.Budget
import kotlinx.android.synthetic.main.item_list_historic.view.*


class HistoricViewHolder(view: View) : RecyclerView.ViewHolder(view){
    fun bind(budget: Budget, listener: (Budget) -> Unit) {
        itemView.apply {
            setOnClickListener { listener(budget) }
            val title = budget.badgeNumber.toString()
            tv_title_ornumb.text = title
            tv_date_h.text = budget.badgeDate.toString()
            tv_clientName.text  =  budget.badgeDestName.toString()
            tv_clientCel.text =  budget.badgeDestEmail.toString()
            val stateListAnimator = AnimatorInflater
                    .loadStateListAnimator(context, R.anim.lift_on_touch)
            this.stateListAnimator = stateListAnimator
        }

    }
}
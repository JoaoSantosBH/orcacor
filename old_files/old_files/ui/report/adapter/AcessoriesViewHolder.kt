package com.jomar.senhorpintor.ui.report.adapter

import android.view.View
import com.jomar.senhorpintor.base.BaseViewHolder
import com.jomar.senhorpintor.model.entities.BudgetReportAcessories
import kotlinx.android.synthetic.main.report_acessories.view.*


class AcessoriesViewHolder(view: View) :
        BaseViewHolder<BudgetReportAcessories>(view){

    override fun bind(ace: BudgetReportAcessories) {
        itemView.apply {
            info.text = ace.info

        }

    }


}
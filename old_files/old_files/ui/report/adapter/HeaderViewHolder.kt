package com.jomar.senhorpintor.ui.report.adapter

import android.view.View
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.BaseViewHolder
import com.jomar.senhorpintor.extensions.show
import com.jomar.senhorpintor.model.entities.BudgeReportHeader
import kotlinx.android.synthetic.main.report_header.view.*

class HeaderViewHolder(view: View) : 
        BaseViewHolder<BudgeReportHeader>(view){

    override fun bind(header: BudgeReportHeader) {
        itemView.apply {
            pincel.show()
            tv_painteer_name.text = header.painterName
            tv_badget_nymber.text = resources.getString(R.string.number) + " " +  header.badgeNumber
            tv_client_name.text = resources.getString(R.string.client_prefix) + header.clientName
            tv_client_email.text = header.clientEmail
            tv_date_h.text = header.date.toString()
            tv_paint_cel.text = header.painterCelPhone
            tv_total_area_value.text = header.totalArea.toString() + " " + resources.getString(R.string.m2)
        }
    }
}
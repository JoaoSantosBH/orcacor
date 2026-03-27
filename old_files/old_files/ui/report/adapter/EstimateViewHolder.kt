package com.jomar.senhorpintor.ui.report.adapter

import android.view.View
import com.jomar.senhorpintor.base.BaseViewHolder
import com.jomar.senhorpintor.dto.EstimateDTO
import kotlinx.android.synthetic.main.report_material.view.*


class EstimateViewHolder(view: View) :
            BaseViewHolder<EstimateDTO>(view) {
        override fun bind(estimating: EstimateDTO) {
            itemView.apply {
                tv_rooms.text = estimating.name
                info.text = estimating.info
            }
        }
    }
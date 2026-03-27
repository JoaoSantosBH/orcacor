package com.jomar.senhorpintor.ui.report.adapter

import android.view.View
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.BaseViewHolder
import com.jomar.senhorpintor.model.entities.BudgetReportMaterial
import kotlinx.android.synthetic.main.report_material.view.*

class MaterialViewHolder(view: View) :
        BaseViewHolder<BudgetReportMaterial>(view) {

    override fun bind(material: BudgetReportMaterial) {
            itemView.apply {
                var value = String.format("%.2f", material.totalArea)

                tv_rooms.text = material.materialName
                tv_material_area_value.text = value + " " + resources.getString(R.string.m2)

                info.text = material.info
                value = String.format("%.2f",material.diff)
                dif.text = resources.getString(R.string.diff_txt) + value + resources.getString(R.string.liters)
                yedel.text = material.totalLiter.toString() + resources.getString(R.string.liters) + "\n"+ resources.getString(R.string.yeld_txt) + " "+ material.yeld.toString() + " " + resources.getString(R.string.m_l)
            }
    }
}
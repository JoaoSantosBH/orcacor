package com.jomar.senhorpintor.ui.report.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.BaseViewHolder
import com.jomar.senhorpintor.dto.EstimateDTO
import com.jomar.senhorpintor.model.entities.BudgeReportHeader
import com.jomar.senhorpintor.model.entities.BudgetReportAcessories
import com.jomar.senhorpintor.model.entities.BudgetReportMaterial
import com.jomar.senhorpintor.model.entities.Room

class ReportAdapter(
        private val context: Context
) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    var list: List<Any> = emptyList()
    private val HEADER = 0
    private val ROOM = 1
    private val ACESSORIE = 2
    private val MATERIAL = 3
    private val ESTIMATING =4

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {

        return when (viewType) {
            HEADER -> {
                val view = LayoutInflater.from(context)
                        .inflate(R.layout.report_header, parent, false)
                HeaderViewHolder(view)
            }
            ROOM -> {
                val view = LayoutInflater.from(context)
                        .inflate(R.layout.report_room, parent, false)
                RoomViewHolder(view)
            }
            ACESSORIE -> {
                val view = LayoutInflater.from(context)
                        .inflate(R.layout.report_acessories, parent, false)
                AcessoriesViewHolder(view)
            }
            MATERIAL -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.report_material, parent, false)
                MaterialViewHolder(view)
            }
            ESTIMATING -> {
                val view = LayoutInflater.from(context)
                        .inflate(R.layout.report_material, parent, false)
                EstimateViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int = list.size
     fun setData(myList: List<Any>){
        list = myList
    }
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val element = list[position]

        when (holder) {
            is HeaderViewHolder -> holder.bind(element as BudgeReportHeader)
            is AcessoriesViewHolder -> holder.bind(element as BudgetReportAcessories)
            is RoomViewHolder -> holder.bind(element as Room)
            is MaterialViewHolder -> holder.bind(element as BudgetReportMaterial)
            is EstimateViewHolder -> holder.bind(element as EstimateDTO)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val comparable = list[position]
        return when (comparable) {
            is BudgeReportHeader -> HEADER
            is Room -> ROOM
            is BudgetReportAcessories -> ACESSORIE
            is BudgetReportMaterial -> MATERIAL
            is EstimateDTO -> ESTIMATING
            else -> throw IllegalArgumentException("Invalid type of data " + position)
        }
    }

}
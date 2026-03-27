package com.jomar.senhorpintor.ui.historic.adapter

import android.view.ViewGroup
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.BaseAdapter
import com.jomar.senhorpintor.model.entities.Budget

class HistoricAdapter(
        private val items: MutableList<Budget>,
        private val listener: (Budget) -> Unit = {}
): BaseAdapter<HistoricViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoricViewHolder =
            HistoricViewHolder(parent.inflateLayout(R.layout.item_list_historic))

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HistoricViewHolder, position: Int) {
        holder.bind(items[position], listener)
    }
    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

}
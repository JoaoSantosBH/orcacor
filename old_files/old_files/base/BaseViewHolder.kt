package com.jomar.senhorpintor.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(view: View): RecyclerView.ViewHolder(view), OnBindViewHolder<T> {

    override fun bind(item: T){
        itemView.setOnClickListener{  }
    }
}
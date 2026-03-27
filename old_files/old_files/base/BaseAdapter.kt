package com.jomar.senhorpintor.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {

    private var lastLoadedPosition = -1

    protected fun setAppearingAnimation(position: Int, viewToAnimate: View) {
        if (position > lastLoadedPosition) {
            val animation =
                AnimationUtils.loadAnimation(viewToAnimate.context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastLoadedPosition = position
        }
    }

    protected fun ViewGroup.inflateLayout(@LayoutRes id: Int): View =
        LayoutInflater.from(context).inflate(id, this, false)
}

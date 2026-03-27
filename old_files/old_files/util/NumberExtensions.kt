package com.jomar.senhorpintor.util

import android.widget.TextView
import java.util.*

fun getNumberList(): ArrayList<Int> {
    val list = ArrayList<Int>()
    for (i in 0..5) {
        list.add(i)
    }
    return list
}

fun TextView.toFloat(): Float{
    return this.text.toString().toFloat()
}
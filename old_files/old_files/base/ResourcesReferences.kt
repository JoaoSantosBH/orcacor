package com.jomar.senhorpintor.base

import android.app.Application
import androidx.annotation.StringRes

class ResourcesReferences(private val app: Application) {
    fun getString(@StringRes stringId: Int) = app.getString(stringId)
}
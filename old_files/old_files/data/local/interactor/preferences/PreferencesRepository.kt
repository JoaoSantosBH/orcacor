package com.jomar.senhorpintor.data.local.interactor.budget.preferences

import android.content.SharedPreferences

interface PreferencesRepository {
    val preferences: SharedPreferences
    fun isFirstRunning(): Boolean
    fun makeFirstRunning()
}
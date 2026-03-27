package com.jomar.senhorpintor.data.local.interactor.budget.preferences

import android.content.Context
import android.content.SharedPreferences
import com.jomar.senhorpintor.base.PREF_KEY

class PreferencesRepositoryImpl(private val context: Context) : PreferencesRepository {
    companion object {
        const val FIRST_RUNNING_KEY = "firstRunning"
    }

    override val preferences: SharedPreferences
        get() = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)

    override fun isFirstRunning(): Boolean {
        if (!preferences.contains(FIRST_RUNNING_KEY))
            return true
        return false
    }

    override fun makeFirstRunning(){
        with(preferences.edit()) {
            putBoolean(FIRST_RUNNING_KEY, true)
            apply()
        }
    }
}
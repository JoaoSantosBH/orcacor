package com.jomar.senhorpintor.data.local.interactor.budget.preferences


interface PreferencesInteractor {
    fun isFirstRunning(): Boolean
    fun makeFirstRunning()
}
package com.jomar.senhorpintor.presentation.splash

import android.app.Application
import com.jomar.senhorpintor.base.BaseViewModel

import com.jomar.senhorpintor.data.local.interactor.budget.preferences.PreferencesRepository

class SplashViewModel(app: Application, private val preferencesRepository: PreferencesRepository) :
    BaseViewModel(app) {

    fun isFirstRunning(): Boolean = preferencesRepository.isFirstRunning()
}
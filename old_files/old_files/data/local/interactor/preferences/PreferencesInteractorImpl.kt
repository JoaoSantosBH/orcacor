package com.jomar.senhorpintor.data.local.interactor.budget.preferences

class PreferencesInteractorImpl(private val preferencesRepository: PreferencesRepository) :
        PreferencesInteractor {

    override fun isFirstRunning(): Boolean = preferencesRepository.isFirstRunning()
    override fun makeFirstRunning() = preferencesRepository.makeFirstRunning()
}
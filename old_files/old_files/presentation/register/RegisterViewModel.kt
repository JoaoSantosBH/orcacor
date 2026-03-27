package com.jomar.senhorpintor.presentation.register

import android.app.Application
import com.jomar.senhorpintor.base.BaseViewModel
import com.jomar.senhorpintor.data.local.interactor.budget.preferences.PreferencesRepository
import com.jomar.senhorpintor.data.local.interactor.budget.user.UserInteractor
import com.jomar.senhorpintor.model.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterViewModel (app: Application, private val preferencesRepository: PreferencesRepository, private val userInteractor: UserInteractor) :
BaseViewModel(app) {

    lateinit var myUser: User
     suspend fun saveUser(user: User)= withContext(Dispatchers.IO){
               userInteractor.saveUser(user)
    }

    suspend fun updateUser(user: User) = withContext(Dispatchers.IO){
        userInteractor.updateUser(user)
    }

    suspend fun getUser() = withContext(Dispatchers.IO){
        myUser = userInteractor.getUser()
    }

    suspend fun makeFirstAccess() = withContext(Dispatchers.IO){
        preferencesRepository.makeFirstRunning()
    }
}

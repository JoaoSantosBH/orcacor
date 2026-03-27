package com.jomar.senhorpintor.data.local.interactor.budget.user

import com.jomar.senhorpintor.model.entities.User

class UserInteractorImpl(private val userRepository: UserRepository): UserInteractor {
    override suspend fun saveUser(user: User) {
       userRepository.saveUser(user)
    }

    override suspend fun getUser(): User {
        return userRepository.getUser()
    }
    override suspend fun updateUser(user: User) {
        userRepository.updateUser(user)
    }
}
package com.jomar.senhorpintor.data.local.interactor.budget.user

import com.jomar.senhorpintor.model.entities.User

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUser(): User
    suspend fun updateUser(user: User)
}
package com.jomar.senhorpintor.data.local.interactor.budget.user

import android.content.Context
import com.jomar.senhorpintor.dao.user.UserDao
import com.jomar.senhorpintor.model.entities.User

class UserRepositoryImpl(private val context: Context, private val userDao: UserDao) : UserRepository {
    override suspend fun saveUser(user: User) {
        userDao.insertUser(user)
    }

    override suspend fun getUser(): User {
        return userDao.getUser()
    }

    override suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}
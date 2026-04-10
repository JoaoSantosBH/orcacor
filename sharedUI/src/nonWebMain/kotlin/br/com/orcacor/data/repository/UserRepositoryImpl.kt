package br.com.orcacor.data.repository

import br.com.orcacor.data.local.dao.UserDao
import br.com.orcacor.data.local.mapper.toDomain
import br.com.orcacor.data.local.mapper.toEntity
import br.com.orcacor.domain.entity.User
import br.com.orcacor.domain.repository.UserRepository

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {

    override suspend fun getUser(): User? = userDao.getUser()?.toDomain()

    override suspend fun saveUser(user: User) {
        userDao.upsert(user.toEntity())
    }

    override suspend fun clearUser() {
        userDao.clear()
    }
}

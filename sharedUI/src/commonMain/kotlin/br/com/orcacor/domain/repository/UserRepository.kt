package br.com.orcacor.domain.repository

import br.com.orcacor.domain.entity.User

interface UserRepository {
    suspend fun getUser(): User?
    suspend fun saveUser(user: User)
    suspend fun clearUser()
}

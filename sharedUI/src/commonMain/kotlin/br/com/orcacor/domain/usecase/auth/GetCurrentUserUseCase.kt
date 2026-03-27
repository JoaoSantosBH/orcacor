package br.com.orcacor.domain.usecase.auth

import br.com.orcacor.domain.entity.User
import br.com.orcacor.domain.repository.UserRepository

class GetCurrentUserUseCase(private val userRepository: UserRepository) {

    suspend fun invoke(): User? = userRepository.getUser()
}

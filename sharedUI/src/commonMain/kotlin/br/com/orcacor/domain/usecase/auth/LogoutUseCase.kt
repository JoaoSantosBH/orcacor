package br.com.orcacor.domain.usecase.auth

import br.com.orcacor.domain.repository.UserRepository

class LogoutUseCase(private val userRepository: UserRepository) {

    suspend fun invoke(): Result<Unit> = runCatching {
        userRepository.clearUser()
    }
}

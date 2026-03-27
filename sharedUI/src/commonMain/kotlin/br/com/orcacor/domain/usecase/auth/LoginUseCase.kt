package br.com.orcacor.domain.usecase.auth

import br.com.orcacor.domain.entity.User
import br.com.orcacor.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {

    suspend fun invoke(email: String, password: String): Result<User> = runCatching {
        require(email.isNotBlank()) { "E-mail obrigatório" }
        require(password.length >= 6) { "Senha deve ter ao menos 6 caracteres" }
        // TODO: integrate with backend API
        val user = User(id = "local", name = email.substringBefore("@"), email = email, phone = "")
        userRepository.saveUser(user)
        user
    }
}

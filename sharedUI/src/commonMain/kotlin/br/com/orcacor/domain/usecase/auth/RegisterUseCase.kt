package br.com.orcacor.domain.usecase.auth

import br.com.orcacor.domain.entity.User
import br.com.orcacor.domain.repository.UserRepository
import br.com.orcacor.domain.usecase.budget.randomUuid

class RegisterUseCase(private val userRepository: UserRepository) {

    suspend fun invoke(name: String, email: String, phone: String, password: String): Result<User> = runCatching {
        require(name.isNotBlank()) { "Nome obrigatório" }
        require(email.contains("@")) { "E-mail inválido" }
        require(phone.isNotBlank()) { "Celular obrigatório" }
        require(password.length >= 6) { "Senha deve ter ao menos 6 caracteres" }
        // TODO: integrate with backend API
        val user = User(id = randomUuid(), name = name, email = email, phone = phone)
        userRepository.saveUser(user)
        user
    }
}

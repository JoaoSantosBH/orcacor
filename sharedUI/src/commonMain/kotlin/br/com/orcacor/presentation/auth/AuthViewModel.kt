package br.com.orcacor.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orcacor.domain.entity.User
import br.com.orcacor.domain.usecase.auth.GetCurrentUserUseCase
import br.com.orcacor.domain.usecase.auth.LoginUseCase
import br.com.orcacor.domain.usecase.auth.LogoutUseCase
import br.com.orcacor.domain.usecase.auth.RegisterUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

sealed interface AuthIntent {
    data class Login(val email: String, val password: String) : AuthIntent
    data class Register(val name: String, val email: String, val phone: String, val password: String) : AuthIntent
    object Logout : AuthIntent
    object CheckSession : AuthIntent
    object ClearError : AuthIntent
}

sealed interface AuthEffect {
    object NavigateToHome : AuthEffect
    object NavigateToLogin : AuthEffect
    object NavigateToRegister : AuthEffect
}

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _effects = Channel<AuthEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: AuthIntent) {
        when (intent) {
            is AuthIntent.Login -> login(intent.email, intent.password)
            is AuthIntent.Register -> register(intent.name, intent.email, intent.phone, intent.password)
            AuthIntent.Logout -> logout()
            AuthIntent.CheckSession -> checkSession()
            AuthIntent.ClearError -> _state.value = _state.value.copy(error = null)
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            loginUseCase.invoke(email, password)
                .onSuccess { user ->
                    _state.value = _state.value.copy(isLoading = false, user = user)
                    _effects.send(AuthEffect.NavigateToHome)
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                }
        }
    }

    private fun register(name: String, email: String, phone: String, password: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            registerUseCase.invoke(name, email, phone, password)
                .onSuccess { user ->
                    _state.value = _state.value.copy(isLoading = false, user = user)
                    _effects.send(AuthEffect.NavigateToHome)
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase.invoke()
            _state.value = AuthState()
            _effects.send(AuthEffect.NavigateToLogin)
        }
    }

    private fun checkSession() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase.invoke()
            _state.value = _state.value.copy(user = user)
            if (user != null) {
                _effects.send(AuthEffect.NavigateToHome)
            }
        }
    }
}

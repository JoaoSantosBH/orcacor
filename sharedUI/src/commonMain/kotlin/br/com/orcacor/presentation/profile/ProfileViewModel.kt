package br.com.orcacor.presentation.profile

import androidx.lifecycle.ViewModel
import br.com.orcacor.domain.entity.User
import br.com.orcacor.domain.repository.UserRepository
import br.com.orcacor.domain.usecase.auth.GetCurrentUserUseCase
import br.com.orcacor.domain.usecase.auth.LogoutUseCase
import br.com.orcacor.util.safeLaunch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

data class ProfileState(
    val user: User? = null,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

sealed interface ProfileIntent {
    object Load : ProfileIntent
    data class UpdateName(val name: String) : ProfileIntent
    data class UpdateEmail(val email: String) : ProfileIntent
    data class UpdatePhone(val phone: String) : ProfileIntent
    object Save : ProfileIntent
    object Logout : ProfileIntent
}

sealed interface ProfileEffect {
    object NavigateToLogin : ProfileEffect
}

class ProfileViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    private val _effects = Channel<ProfileEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        onIntent(ProfileIntent.Load)
    }

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.Load -> load()
            is ProfileIntent.UpdateName -> _state.value = _state.value.copy(name = intent.name)
            is ProfileIntent.UpdateEmail -> _state.value = _state.value.copy(email = intent.email)
            is ProfileIntent.UpdatePhone -> _state.value = _state.value.copy(phone = intent.phone)
            ProfileIntent.Save -> save()
            ProfileIntent.Logout -> logout()
        }
    }

    private fun load() {
        safeLaunch {
            val user = getCurrentUserUseCase.invoke()
            _state.value = _state.value.copy(
                user = user,
                name = user?.name ?: "",
                email = user?.email ?: "",
                phone = user?.phone ?: ""
            )
        }
    }

    private fun save() {
        val s = _state.value
        val user = s.user ?: return
        safeLaunch {
            _state.value = s.copy(isSaving = true)
            runCatching {
                userRepository.saveUser(user.copy(name = s.name, email = s.email, phone = s.phone))
            }.onSuccess {
                _state.value = _state.value.copy(isSaving = false, saveSuccess = true)
            }.onFailure { error ->
                _state.value = _state.value.copy(isSaving = false, error = error.message)
            }
        }
    }

    private fun logout() {
        safeLaunch {
            logoutUseCase.invoke()
            _effects.send(ProfileEffect.NavigateToLogin)
        }
    }
}

package br.com.orcacor.presentation.budget

import androidx.lifecycle.ViewModel
import br.com.orcacor.domain.usecase.budget.CreateBudgetUseCase
import br.com.orcacor.util.safeLaunch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

data class CreateBudgetState(
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface CreateBudgetIntent {
    object CreateNew : CreateBudgetIntent
    object ClearError : CreateBudgetIntent
}

sealed interface CreateBudgetEffect {
    data class NavigateToBudget(val budgetId: String) : CreateBudgetEffect
}

class CreateBudgetViewModel(
    private val createBudgetUseCase: CreateBudgetUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateBudgetState())
    val state: StateFlow<CreateBudgetState> = _state.asStateFlow()

    private val _effects = Channel<CreateBudgetEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: CreateBudgetIntent) {
        when (intent) {
            CreateBudgetIntent.CreateNew -> createNew()
            CreateBudgetIntent.ClearError -> _state.value = _state.value.copy(error = null)
        }
    }

    private fun createNew() {
        safeLaunch {
            _state.value = _state.value.copy(isLoading = true)
            createBudgetUseCase.invoke("", "")
                .onSuccess { budget ->
                    _state.value = _state.value.copy(isLoading = false)
                    _effects.send(CreateBudgetEffect.NavigateToBudget(budget.id))
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                }
        }
    }
}
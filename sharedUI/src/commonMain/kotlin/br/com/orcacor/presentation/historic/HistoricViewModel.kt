package br.com.orcacor.presentation.historic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orcacor.domain.entity.Budget
import br.com.orcacor.domain.usecase.budget.DeleteBudgetUseCase
import br.com.orcacor.domain.usecase.budget.GetBudgetHistoryUseCase
import br.com.orcacor.util.safeLaunch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

data class HistoricState(
    val budgets: List<Budget> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface HistoricIntent {
    data class DeleteBudget(val budgetId: String) : HistoricIntent
    data class OpenBudget(val budgetId: String) : HistoricIntent
}

sealed interface HistoricEffect {
    data class NavigateToReport(val budgetId: String) : HistoricEffect
}

class HistoricViewModel(
    private val getBudgetHistoryUseCase: GetBudgetHistoryUseCase,
    private val deleteBudgetUseCase: DeleteBudgetUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HistoricState(isLoading = true))
    val state: StateFlow<HistoricState> = _state.asStateFlow()

    private val _effects = Channel<HistoricEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        getBudgetHistoryUseCase.invoke()
            .onEach { budgets ->
                _state.value = _state.value.copy(budgets = budgets, isLoading = false)
            }
            .launchIn(viewModelScope)
    }

    fun onIntent(intent: HistoricIntent) {
        when (intent) {
            is HistoricIntent.DeleteBudget -> delete(intent.budgetId)
            is HistoricIntent.OpenBudget -> safeLaunch {
                _effects.send(HistoricEffect.NavigateToReport(intent.budgetId))
            }
        }
    }

    private fun delete(budgetId: String) {
        safeLaunch {
            deleteBudgetUseCase.invoke(budgetId)
        }
    }
}

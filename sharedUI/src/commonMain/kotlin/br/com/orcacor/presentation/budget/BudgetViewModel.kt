package br.com.orcacor.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orcacor.domain.entity.Budget
import br.com.orcacor.domain.entity.Room
import br.com.orcacor.domain.usecase.budget.CreateBudgetUseCase
import br.com.orcacor.domain.usecase.budget.SaveBudgetUseCase
import br.com.orcacor.domain.usecase.room.DeleteRoomUseCase
import br.com.orcacor.domain.repository.RoomRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class BudgetState(
    val budget: Budget? = null,
    val rooms: List<Room> = emptyList(),
    val recipientName: String = "",
    val recipientEmail: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface BudgetIntent {
    object CreateNew : BudgetIntent
    data class UpdateRecipient(val name: String, val email: String) : BudgetIntent
    data class DeleteRoom(val roomId: Long) : BudgetIntent
    object GenerateReport : BudgetIntent
    object ClearError : BudgetIntent
}

sealed interface BudgetEffect {
    data class NavigateToRoomForm(val budgetId: String) : BudgetEffect
    data class NavigateToReport(val budgetId: String) : BudgetEffect
}

class BudgetViewModel(
    private val createBudgetUseCase: CreateBudgetUseCase,
    private val saveBudgetUseCase: SaveBudgetUseCase,
    private val deleteRoomUseCase: DeleteRoomUseCase,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetState())
    val state: StateFlow<BudgetState> = _state.asStateFlow()

    private val _effects = Channel<BudgetEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onIntent(intent: BudgetIntent) {
        when (intent) {
            BudgetIntent.CreateNew -> createNew()
            is BudgetIntent.UpdateRecipient -> updateRecipient(intent.name, intent.email)
            is BudgetIntent.DeleteRoom -> deleteRoom(intent.roomId)
            BudgetIntent.GenerateReport -> generateReport()
            BudgetIntent.ClearError -> _state.value = _state.value.copy(error = null)
        }
    }

    private fun createNew() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            createBudgetUseCase.invoke("", "")
                .onSuccess { budget ->
                    _state.value = _state.value.copy(isLoading = false, budget = budget)
                    observeRooms(budget.id)
                    _effects.send(BudgetEffect.NavigateToRoomForm(budget.id))
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                }
        }
    }

    private fun updateRecipient(name: String, email: String) {
        _state.value = _state.value.copy(recipientName = name, recipientEmail = email)
        val budget = _state.value.budget ?: return
        viewModelScope.launch {
            saveBudgetUseCase.invoke(budget.copy(recipientName = name, recipientEmail = email))
        }
    }

    private fun deleteRoom(roomId: Long) {
        viewModelScope.launch {
            deleteRoomUseCase.invoke(roomId)
        }
    }

    private fun generateReport() {
        val budgetId = _state.value.budget?.id ?: return
        viewModelScope.launch {
            _effects.send(BudgetEffect.NavigateToReport(budgetId))
        }
    }

    private fun observeRooms(budgetId: String) {
        roomRepository.getByBudgetId(budgetId)
            .onEach { rooms ->
                val totalArea = rooms.sumOf { it.totalSquareMeters.toDouble() }.toFloat()
                _state.value = _state.value.copy(rooms = rooms)
                _state.value.budget?.let { budget ->
                    saveBudgetUseCase.invoke(budget.copy(totalArea = totalArea))
                }
            }
            .launchIn(viewModelScope)
    }

    fun loadBudget(budgetId: String) {
        observeRooms(budgetId)
    }
}

package br.com.orcacor.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.orcacor.domain.entity.Budget
import br.com.orcacor.domain.entity.BudgetStatus
import br.com.orcacor.domain.entity.Room
import br.com.orcacor.domain.repository.RoomRepository
import br.com.orcacor.domain.usecase.budget.CreateBudgetUseCase
import br.com.orcacor.domain.usecase.budget.GetBudgetHistoryUseCase
import br.com.orcacor.domain.usecase.budget.SaveBudgetUseCase
import br.com.orcacor.domain.usecase.room.DeleteRoomUseCase
import br.com.orcacor.util.safeLaunch
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow

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
    private val getBudgetHistoryUseCase: GetBudgetHistoryUseCase,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetState())
    val state: StateFlow<BudgetState> = _state.asStateFlow()

    private val _effects = Channel<BudgetEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    private var observeRoomsJob: Job? = null

    init {
        loadExistingDraft()
    }

    fun onIntent(intent: BudgetIntent) {
        when (intent) {
            BudgetIntent.CreateNew -> createNew()
            is BudgetIntent.UpdateRecipient -> updateRecipient(intent.name, intent.email)
            is BudgetIntent.DeleteRoom -> deleteRoom(intent.roomId)
            BudgetIntent.GenerateReport -> generateReport()
            BudgetIntent.ClearError -> _state.value = _state.value.copy(error = null)
        }
    }

    private fun loadExistingDraft() {
        safeLaunch {
            val allBudgets = getBudgetHistoryUseCase.invoke().first()
            val draft = allBudgets.firstOrNull { it.status == BudgetStatus.DRAFT }
            if (draft != null && _state.value.budget == null) {
                _state.value = _state.value.copy(
                    budget = draft,
                    recipientName = draft.recipientName,
                    recipientEmail = draft.recipientEmail
                )
                observeRooms(draft.id)
            }
        }
    }

    private fun createNew() {
        safeLaunch {
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
        safeLaunch {
            saveBudgetUseCase.invoke(budget.copy(recipientName = name, recipientEmail = email))
        }
    }

    private fun deleteRoom(roomId: Long) {
        safeLaunch {
            deleteRoomUseCase.invoke(roomId)
        }
    }

    private fun generateReport() {
        val budgetId = _state.value.budget?.id ?: return
        safeLaunch {
            _effects.send(BudgetEffect.NavigateToReport(budgetId))
        }
    }

    private fun observeRooms(budgetId: String) {
        observeRoomsJob?.cancel()
        observeRoomsJob = roomRepository.getByBudgetId(budgetId)
            .onEach { rooms ->
                val totalArea = rooms.sumOf { it.totalSquareMeters.toDouble() }.toFloat()
                val updatedBudget = _state.value.budget?.copy(totalArea = totalArea)
                _state.value = _state.value.copy(rooms = rooms, budget = updatedBudget)
                updatedBudget?.let { saveBudgetUseCase.invoke(it) }
            }
            .launchIn(viewModelScope)
    }

    fun loadBudget(budgetId: String) {
        observeRooms(budgetId)
    }
}
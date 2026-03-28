package br.com.orcacor.presentation.room

import androidx.lifecycle.ViewModel
import br.com.orcacor.domain.entity.Accessory
import br.com.orcacor.domain.entity.AccessoryType
import br.com.orcacor.domain.entity.ClosetKind
import br.com.orcacor.domain.entity.DoorKind
import br.com.orcacor.domain.entity.MirrorKind
import br.com.orcacor.domain.entity.Room
import br.com.orcacor.domain.entity.RoomKind
import br.com.orcacor.domain.entity.WindowKind
import br.com.orcacor.domain.repository.MaterialConstantsRepository
import br.com.orcacor.domain.usecase.room.AddRoomToBudgetUseCase
import br.com.orcacor.domain.usecase.room.CalculateRoomAreaUseCase
import br.com.orcacor.util.safeLaunch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

data class RoomFormState(
    val budgetId: String = "",
    val name: String = "",
    val kind: RoomKind = RoomKind.SYMMETRIC,
    val note: String = "",
    val wallIsNew: Boolean = false,
    val desiredColor: String = "",
    val width: String = "",
    val height: String = "",
    val length: String = "",
    val irregularWalls: List<Float> = emptyList(),
    val doors: List<Accessory> = emptyList(),
    val windows: List<Accessory> = emptyList(),
    val mirrors: List<Accessory> = emptyList(),
    val closets: List<Accessory> = emptyList(),
    val coats: Int = 2,
    val previewTotalArea: Float = 0f,
    val previewWallsArea: Float = 0f,
    val previewCeilingArea: Float = 0f,
    val windowKinds: List<WindowKind> = emptyList(),
    val doorKinds: List<DoorKind> = emptyList(),
    val mirrorKinds: List<MirrorKind> = emptyList(),
    val closetKinds: List<ClosetKind> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface RoomFormIntent {
    data class SetBudgetId(val budgetId: String) : RoomFormIntent
    data class UpdateName(val name: String) : RoomFormIntent
    data class UpdateKind(val kind: RoomKind) : RoomFormIntent
    data class UpdateNote(val note: String) : RoomFormIntent
    data class UpdateWallIsNew(val isNew: Boolean) : RoomFormIntent
    data class UpdateDesiredColor(val color: String) : RoomFormIntent
    data class UpdateWidth(val value: String) : RoomFormIntent
    data class UpdateHeight(val value: String) : RoomFormIntent
    data class UpdateLength(val value: String) : RoomFormIntent
    data class UpdateIrregularWalls(val walls: List<Float>) : RoomFormIntent
    data class AddAccessory(val type: AccessoryType, val kindId: Int, val quantity: Int, val area: Float) : RoomFormIntent
    data class RemoveAccessory(val type: AccessoryType, val index: Int) : RoomFormIntent
    data class UpdateCoats(val coats: Int) : RoomFormIntent
    object SaveRoom : RoomFormIntent
    object ClearError : RoomFormIntent
}

sealed interface RoomFormEffect {
    object NavigateBack : RoomFormEffect
}

class RoomFormViewModel(
    private val addRoomToBudgetUseCase: AddRoomToBudgetUseCase,
    private val calculateRoomAreaUseCase: CalculateRoomAreaUseCase,
    private val materialConstantsRepository: MaterialConstantsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RoomFormState())
    val state: StateFlow<RoomFormState> = _state.asStateFlow()

    private val _effects = Channel<RoomFormEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        loadKinds()
    }

    fun onIntent(intent: RoomFormIntent) {
        when (intent) {
            is RoomFormIntent.SetBudgetId -> _state.value = _state.value.copy(budgetId = intent.budgetId)
            is RoomFormIntent.UpdateName -> update { copy(name = intent.name) }
            is RoomFormIntent.UpdateKind -> update { copy(kind = intent.kind) }
            is RoomFormIntent.UpdateNote -> update { copy(note = intent.note) }
            is RoomFormIntent.UpdateWallIsNew -> update { copy(wallIsNew = intent.isNew) }
            is RoomFormIntent.UpdateDesiredColor -> update { copy(desiredColor = intent.color) }
            is RoomFormIntent.UpdateWidth -> update { copy(width = intent.value) }
            is RoomFormIntent.UpdateHeight -> update { copy(height = intent.value) }
            is RoomFormIntent.UpdateLength -> update { copy(length = intent.value) }
            is RoomFormIntent.UpdateIrregularWalls -> update { copy(irregularWalls = intent.walls) }
            is RoomFormIntent.UpdateCoats -> update { copy(coats = intent.coats) }
            is RoomFormIntent.AddAccessory -> addAccessory(intent.type, intent.kindId, intent.quantity, intent.area)
            is RoomFormIntent.RemoveAccessory -> removeAccessory(intent.type, intent.index)
            RoomFormIntent.SaveRoom -> saveRoom()
            RoomFormIntent.ClearError -> update { copy(error = null) }
        }
        recalculatePreview()
    }

    private fun update(transform: RoomFormState.() -> RoomFormState) {
        _state.value = _state.value.transform()
    }

    private fun addAccessory(type: AccessoryType, kindId: Int, quantity: Int, area: Float) {
        val accessory = Accessory(type = type, kindId = kindId, quantity = quantity, area = area * quantity)
        update {
            when (type) {
                AccessoryType.DOOR -> copy(doors = doors + accessory)
                AccessoryType.WINDOW -> copy(windows = windows + accessory)
                AccessoryType.MIRROR -> copy(mirrors = mirrors + accessory)
                AccessoryType.CLOSET -> copy(closets = closets + accessory)
            }
        }
    }

    private fun removeAccessory(type: AccessoryType, index: Int) {
        update {
            when (type) {
                AccessoryType.DOOR -> copy(doors = doors.toMutableList().also { it.removeAt(index) })
                AccessoryType.WINDOW -> copy(windows = windows.toMutableList().also { it.removeAt(index) })
                AccessoryType.MIRROR -> copy(mirrors = mirrors.toMutableList().also { it.removeAt(index) })
                AccessoryType.CLOSET -> copy(closets = closets.toMutableList().also { it.removeAt(index) })
            }
        }
    }

    private fun recalculatePreview() {
        val s = _state.value
        val room = s.toRoom()
        val calculated = calculateRoomAreaUseCase.invoke(room)
        _state.value = _state.value.copy(
            previewTotalArea = calculated.totalSquareMeters,
            previewWallsArea = calculated.wallsArea,
            previewCeilingArea = calculated.ceilingArea
        )
    }

    private fun saveRoom() {
        val s = _state.value
        if (s.name.isBlank()) {
            _state.value = s.copy(error = "Nome do cômodo obrigatório")
            return
        }
        safeLaunch {
            _state.value = _state.value.copy(isLoading = true)
            addRoomToBudgetUseCase.invoke(s.toRoom())
                .onSuccess {
                    _state.value = _state.value.copy(isLoading = false)
                    _effects.send(RoomFormEffect.NavigateBack)
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(isLoading = false, error = error.message)
                }
        }
    }

    private fun loadKinds() {
        safeLaunch {
            _state.value = _state.value.copy(
                windowKinds = materialConstantsRepository.getWindowKinds(),
                doorKinds = materialConstantsRepository.getDoorKinds(),
                mirrorKinds = materialConstantsRepository.getMirrorKinds(),
                closetKinds = materialConstantsRepository.getClosetKinds()
            )
        }
    }

    private fun RoomFormState.toRoom() = Room(
        budgetId = budgetId,
        name = name,
        kind = kind,
        note = note.takeIf { it.isNotBlank() },
        wallIsNew = wallIsNew,
        desiredColor = desiredColor.takeIf { it.isNotBlank() },
        width = width.toFloatOrNull(),
        height = height.toFloatOrNull(),
        length = length.toFloatOrNull(),
        irregularWalls = irregularWalls,
        doors = doors,
        windows = windows,
        mirrors = mirrors,
        closets = closets,
        coats = coats
    )
}

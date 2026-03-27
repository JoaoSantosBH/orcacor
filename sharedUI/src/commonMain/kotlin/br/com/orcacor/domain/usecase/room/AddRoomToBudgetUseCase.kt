package br.com.orcacor.domain.usecase.room

import br.com.orcacor.domain.entity.Room
import br.com.orcacor.domain.repository.BudgetRepository
import br.com.orcacor.domain.repository.RoomRepository

class AddRoomToBudgetUseCase(
    private val roomRepository: RoomRepository,
    private val budgetRepository: BudgetRepository,
    private val calculateRoomArea: CalculateRoomAreaUseCase
) {
    suspend fun invoke(room: Room): Result<Room> = runCatching {
        val calculated = calculateRoomArea.invoke(room)
        val saved = roomRepository.save(calculated)

        // Update budget total area
        val budget = budgetRepository.getById(room.budgetId)
        if (budget != null) {
            val allRooms = mutableListOf<Room>()
            // We recalculate total from scratch on the budget level in the ViewModel
            budgetRepository.save(budget.copy(totalArea = budget.totalArea + saved.totalSquareMeters))
        }
        saved
    }
}

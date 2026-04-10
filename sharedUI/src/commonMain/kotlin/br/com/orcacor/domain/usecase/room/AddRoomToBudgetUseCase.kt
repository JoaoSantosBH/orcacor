package br.com.orcacor.domain.usecase.room

import br.com.orcacor.domain.entity.Room
import br.com.orcacor.domain.repository.RoomRepository

class AddRoomToBudgetUseCase(
    private val roomRepository: RoomRepository,
    private val calculateRoomArea: CalculateRoomAreaUseCase
) {
    suspend fun invoke(room: Room): Result<Room> = runCatching {
        val calculated = calculateRoomArea.invoke(room)
        roomRepository.save(calculated)
    }
}

package br.com.orcacor.domain.usecase.room

import br.com.orcacor.domain.repository.RoomRepository

class DeleteRoomUseCase(private val roomRepository: RoomRepository) {

    suspend fun invoke(roomId: Long): Result<Unit> = runCatching {
        roomRepository.delete(roomId)
    }
}

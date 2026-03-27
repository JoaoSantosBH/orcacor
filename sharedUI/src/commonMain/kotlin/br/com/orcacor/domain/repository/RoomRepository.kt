package br.com.orcacor.domain.repository

import br.com.orcacor.domain.entity.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    fun getByBudgetId(budgetId: String): Flow<List<Room>>
    suspend fun save(room: Room): Room
    suspend fun delete(roomId: Long)
    suspend fun deleteByBudgetId(budgetId: String)
}

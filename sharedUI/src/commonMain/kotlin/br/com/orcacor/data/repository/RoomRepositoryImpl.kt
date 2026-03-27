package br.com.orcacor.data.repository

import br.com.orcacor.data.local.dao.AccessoryDao
import br.com.orcacor.data.local.dao.RoomDao
import br.com.orcacor.data.local.mapper.toDomain
import br.com.orcacor.data.local.mapper.toAccessoryEntities
import br.com.orcacor.data.local.mapper.toEntity
import br.com.orcacor.domain.entity.Room
import br.com.orcacor.domain.repository.RoomRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl(
    private val roomDao: RoomDao,
    private val accessoryDao: AccessoryDao
) : RoomRepository {

    override fun getByBudgetId(budgetId: String): Flow<List<Room>> =
        roomDao.getByBudgetId(budgetId).map { entities ->
            entities.map { entity ->
                val accessories = accessoryDao.getByRoomId(entity.id)
                entity.toDomain(accessories)
            }
        }

    override suspend fun save(room: Room): Room {
        val entity = room.toEntity()
        val newId = roomDao.upsert(entity)
        val resolvedId = if (room.id != null && room.id != 0L) room.id else newId
        // Replace accessories
        accessoryDao.deleteByRoomId(resolvedId)
        val accessoryEntities = room.toAccessoryEntities(resolvedId)
        if (accessoryEntities.isNotEmpty()) {
            accessoryDao.insertAll(accessoryEntities)
        }
        return room.copy(id = resolvedId)
    }

    override suspend fun delete(roomId: Long) {
        roomDao.deleteById(roomId)
    }

    override suspend fun deleteByBudgetId(budgetId: String) {
        roomDao.deleteByBudgetId(budgetId)
    }
}

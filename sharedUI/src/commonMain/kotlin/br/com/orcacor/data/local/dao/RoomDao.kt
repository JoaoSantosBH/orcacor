package br.com.orcacor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.orcacor.data.local.entity.RoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {

    @Query("SELECT * FROM rooms WHERE budgetId = :budgetId")
    fun getByBudgetId(budgetId: String): Flow<List<RoomEntity>>

    @Query("SELECT * FROM rooms WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): RoomEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(room: RoomEntity): Long

    @Query("DELETE FROM rooms WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM rooms WHERE budgetId = :budgetId")
    suspend fun deleteByBudgetId(budgetId: String)
}

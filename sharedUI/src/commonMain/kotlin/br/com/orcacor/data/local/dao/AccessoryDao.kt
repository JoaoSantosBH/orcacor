package br.com.orcacor.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.orcacor.data.local.entity.AccessoryEntity

@Dao
interface AccessoryDao {

    @Query("SELECT * FROM accessories WHERE roomId = :roomId")
    suspend fun getByRoomId(roomId: Long): List<AccessoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(accessories: List<AccessoryEntity>)

    @Query("DELETE FROM accessories WHERE roomId = :roomId")
    suspend fun deleteByRoomId(roomId: Long)
}

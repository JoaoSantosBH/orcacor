package com.jomar.senhorpintor.dao.room

import androidx.room.*
import com.jomar.senhorpintor.model.entities.Room

@Dao
interface RoomDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRoom(room: Room)

    @Update
    fun updateRoom(room: Room)

   @Delete
    fun deleteRoom(room: Room)

   @Query("SELECT * FROM Room WHERE id = :id" )
    fun getRoom(id: Long): Room

    @Query("SELECT * FROM Room WHERE orderNumber = :orderNumber" )
    fun getBudgetRooms(orderNumber: String): List<Room>

    @Query("delete from Room where orderNumber = :orderNumber")
    fun deleteBudgetRooms(orderNumber: String)
}
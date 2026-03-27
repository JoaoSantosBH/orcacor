package com.jomar.senhorpintor.data.local.interactor.rooms

import android.content.Context
import com.jomar.senhorpintor.dao.room.RoomDao
import com.jomar.senhorpintor.model.entities.Room

class RoomsRepositoryImpl(private val context: Context, private val roomDao: RoomDao) : RoomRepository {
    override suspend fun insertRoom(room: Room){
        roomDao.insertRoom(room)
    }

    override suspend fun getBudgetRooms(number: String): List<Room> {
        return roomDao.getBudgetRooms(number)
    }
    override suspend fun deleteBudgetRooms(number: String) {
        roomDao.deleteBudgetRooms(number)
    }
}
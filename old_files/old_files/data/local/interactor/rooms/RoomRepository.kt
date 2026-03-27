package com.jomar.senhorpintor.data.local.interactor.rooms

import com.jomar.senhorpintor.model.entities.Room

interface RoomRepository {
    suspend fun insertRoom(room: Room)
    suspend fun getBudgetRooms(number: String): List<Room>
    suspend fun deleteBudgetRooms(number: String)
}
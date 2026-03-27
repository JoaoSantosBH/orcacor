package com.jomar.senhorpintor.data.local.interactor.rooms

import com.jomar.senhorpintor.model.entities.Room

class RoomInteractorImpl(private val roomRepository: RoomRepository) : RoomInteractor {
    override suspend fun insertRoom(room: Room){
        roomRepository.insertRoom(room)
    }

    override suspend fun getBudgetRooms(number: String): List<Room> {
        return roomRepository.getBudgetRooms(number)
    }

    override suspend fun deleteBudgetRooms(number: String) {
        roomRepository.deleteBudgetRooms(number)
    }
}
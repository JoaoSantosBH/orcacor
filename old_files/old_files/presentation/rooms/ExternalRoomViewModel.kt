package com.jomar.senhorpintor.presentation.rooms

import android.app.Application
import com.jomar.senhorpintor.base.App
import com.jomar.senhorpintor.base.BaseViewModel
import com.jomar.senhorpintor.base.DOOR
import com.jomar.senhorpintor.base.WINDOW
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getDoorJsonList
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getWindowJsonList
import com.jomar.senhorpintor.data.local.interactor.rooms.RoomInteractor
import com.jomar.senhorpintor.model.entities.DoorKind
import com.jomar.senhorpintor.model.entities.Room
import com.jomar.senhorpintor.model.entities.WindowKind
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExternalRoomViewModel(app: Application, private val interactor: RoomInteractor) : BaseViewModel(app) {

    lateinit var myRoom: Room
    var base: Float = 0f
    var janelas: Float = 0f
    var portas: Float = 0f
    var teto: Float = 0f
    var metr2total: Float = 0f

    private lateinit var doorListJ: List<DoorKind>
    private lateinit var windowList: List<WindowKind>

    suspend fun insertExternalRoom(room: Room) = withContext(Dispatchers.IO) {
        myRoom = configureExternalRoom(room)
        interactor.insertRoom(myRoom)
    }

    private fun configureExternalRoom(room: Room): Room {
        windowList = getWindowJsonList(App.instance, WINDOW)
        doorListJ = getDoorJsonList(App.instance, DOOR)

        if (room.windowNumber!! > 0) {
            val result: List<WindowKind> = windowList.filter { w -> w.id == room.windowKind }
            janelas = room.windowNumber!! * result.get(0).area
        }

        if (room.doorNumber!! > 0) {
            val result: List<DoorKind> = doorListJ.filter { w -> w.id == room.doorKind }
            portas = room.doorNumber!! * result.get(0).area
        }
        room.teto = 0f
        base = room.width!! * room.height!!
        metr2total = base - janelas - portas
        room.base = base
        room.totalSquareMETER = metr2total
        return room
    }
}

package com.jomar.senhorpintor.presentation.rooms

import android.app.Application
import com.jomar.senhorpintor.base.*
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getClosetJsonList
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getDoorJsonList
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getMirrorJsonList
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getWindowJsonList
import com.jomar.senhorpintor.data.local.interactor.rooms.RoomInteractor
import com.jomar.senhorpintor.model.entities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SimetricRoomViewModel(app: Application, private val interactor: RoomInteractor) : BaseViewModel(app) {
    lateinit var myRoom: Room
    var base: Float = 0f
    var paredes: Float = 0f
    var janelas: Float = 0f
    var portas: Float = 0f
    var espelhos: Float = 0f
    var armarios: Float = 0f
    var teto: Float = 0f
    var metr2total: Float = 0f

    private lateinit var closetList: List<ClosetKind>
    private lateinit var doorListJ: List<DoorKind>
    private lateinit var mirrorList: List<MirrorKind>
    private lateinit var windowList: List<WindowKind>


    suspend fun insertSimetricRoom(room: Room) = withContext(Dispatchers.IO) {
        myRoom = configureSimetricRoom(room)
        interactor.insertRoom(myRoom)
    }

    private fun configureSimetricRoom(room: Room): Room {
        windowList = getWindowJsonList(App.instance, WINDOW)
        doorListJ = getDoorJsonList(App.instance, DOOR)
        mirrorList = getMirrorJsonList(App.instance, MIRROR)
        closetList = getClosetJsonList(App.instance, CLOSET)

        if (room.windowNumber!! > 0) {
            val result: List<WindowKind> = windowList.filter { w -> w.id == room.windowKind }
            janelas = room.windowNumber!! * result.get(0).area
        }

        if (room.doorNumber!! > 0) {
            val result: List<DoorKind> = doorListJ.filter { w -> w.id == room.doorKind }
            portas = room.doorNumber!! * result.get(0).area
        }

        if (room.closetNumber!! > 0) {
            val result: List<ClosetKind> = closetList.filter { w -> w.id == room.closetKind }
            armarios = room.closetNumber!! * result.get(0).area
        }

        if (room.mirrorNumber!! > 0) {
            val result: List<MirrorKind> = mirrorList.filter { w -> w.id == room.mirrorKind }
            espelhos = room.mirrorNumber!! * result.get(0).area
        }
        paredes = ((room.width!! * 2) * room.height!!) + ((room.lenght!! * 2) * room.height!!)
        teto = room.width * room.lenght
        room.teto = teto
        base = paredes + teto
        metr2total = base - janelas - portas - espelhos - armarios
        room.base = base
        room.totalSquareMETER = metr2total
        return room
    }
}


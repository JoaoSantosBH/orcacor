@file:Suppress("KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation", "KotlinDeprecation")

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

class AssimetricRoomViewModel(app: Application, private val interactor: RoomInteractor) : BaseViewModel(app) {
    private lateinit var myRoom: Room
    var base: Float = 0f
    private var paredes: Float = 0f
    private var janelas: Float = 0f
    private var portas: Float = 0f
    private var espelhos: Float = 0f
    private var armarios: Float = 0f
    private var teto: Float = 0f
    private var metr2total: Float = 0f
    private var somatoria: Float = 0f
    private var media: Float = 0f

    private lateinit var closetList: List<ClosetKind>
    private lateinit var doorListJ: List<DoorKind>
    private lateinit var mirrorList: List<MirrorKind>
    private lateinit var windowList: List<WindowKind>


    suspend fun insertAssimetricRoom(doorList: List<Float>, room: Room) = withContext(Dispatchers.IO) {
        myRoom = configureAssimetricRoom(doorList, room)
        interactor.insertRoom(myRoom)
    }

    private fun configureAssimetricRoom(doorList: List<Float>, room: Room): Room {
        windowList = getWindowJsonList(App.instance, WINDOW)
        doorListJ = getDoorJsonList(App.instance, DOOR)
        mirrorList = getMirrorJsonList(App.instance, MIRROR)
        closetList = getClosetJsonList(App.instance, CLOSET)

        if (room.windowNumber!! > 0) {
            val result: List<WindowKind> = windowList.filter { w -> w.id == room.windowKind }
            janelas = room.windowNumber!! * result[0].area
        }

        if (room.doorNumber!! > 0) {
            val result: List<DoorKind> = doorListJ.filter { w -> w.id == room.doorKind }
            portas = room.doorNumber!! * result[0].area
        }

        if (room.closetNumber!! > 0) {
            val result: List<ClosetKind> = closetList.filter { w -> w.id == room.closetKind }
            armarios = room.closetNumber!! * result[0].area
        }

        if (room.mirrorNumber!! > 0) {
            val result: List<MirrorKind> = mirrorList.filter { w -> w.id == room.mirrorKind }
            espelhos = room.mirrorNumber!! * result[0].area
        }

        for (i in doorList.indices) {
            somatoria = somatoria?.plus(doorList[i])
        }

        paredes = somatoria?.times(room.height!!)
        media = somatoria?.div(4)

        teto = media?.times(media!!)
        room.teto = teto
        base = paredes!! - janelas!! - portas!! - espelhos!! - armarios!!
        metr2total = base!! + teto!!
        room.base = base
        room.totalSquareMETER = metr2total
        return room
    }


}
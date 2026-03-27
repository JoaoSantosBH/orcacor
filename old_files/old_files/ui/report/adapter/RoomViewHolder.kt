package com.jomar.senhorpintor.ui.report.adapter

import android.view.View
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.App
import com.jomar.senhorpintor.base.BaseViewHolder
import com.jomar.senhorpintor.base.ROOM
import com.jomar.senhorpintor.data.GetJsonDataService.Companion.getRoomJsonList
import com.jomar.senhorpintor.extensions.show
import com.jomar.senhorpintor.model.entities.Room
import kotlinx.android.synthetic.main.report_room.view.*


class RoomViewHolder(view: View) :
        BaseViewHolder<Room>(view) {
    val rooms = getRoomJsonList(App.instance, ROOM)

    override fun bind(room: Room) {
        val name = rooms.filter { r ->
            r.id == room.idRoom?.toInt()
        }
            itemView.apply {
                tv_rooms.text = name.toString()
                val value = String.format("%.2f", room.totalSquareMETER)
                tv_material_area_value.text = value + " " + resources.getString(R.string.m2)
                info.text = room.note
                if (room.wallIsNew!!){
                    reboco.text = resources.getString(R.string.reboco)
                    reboco.show()
                }
                if (room.demaos!! > 0){
                    tv_demao.text = room.demaos.toString() + " " +  resources.getString(R.string.demao)
                    tv_demao.show()
                }
            }


    }




}
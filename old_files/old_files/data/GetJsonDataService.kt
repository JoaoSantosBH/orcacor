package com.jomar.senhorpintor.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jomar.senhorpintor.R
import com.jomar.senhorpintor.base.*
import com.jomar.senhorpintor.model.entities.*

class GetJsonDataService {
    companion object{
        fun getRoomJsonList(context: Context, file: String): List<RoomKind> {
            val raw = getRawFile(file)
            val resources = context.resources
            val inputStream = resources.openRawResource(raw)

            inputStream.bufferedReader().use {
                val json = it.readText()
                val comodos = fromJson<List<RoomKind>>(json)
                return comodos
            }
        }

        fun getClosetJsonList(context: Context, file: String): List<ClosetKind> {
            val raw = getRawFile(file)
            val resources = context.resources
            val inputStream = resources.openRawResource(raw)

            inputStream.bufferedReader().use {
                val json = it.readText()
                val comodos = fromJson<List<ClosetKind>>(json)
                return comodos
            }
        }

        fun getDoorJsonList(context: Context, file: String): List<DoorKind> {
            val raw = getRawFile(file)
            val resources = context.resources
            val inputStream = resources.openRawResource(raw)

            inputStream.bufferedReader().use {
                val json = it.readText()
                val comodos = fromJson<List<DoorKind>>(json)
                return comodos
            }
        }


        fun getMirrorJsonList(context: Context, file: String): List<MirrorKind> {
            val raw = getRawFile(file)
            val resources = context.resources
            val inputStream = resources.openRawResource(raw)

            inputStream.bufferedReader().use {
                val json = it.readText()
                val comodos = fromJson<List<MirrorKind>>(json)
                return comodos
            }
        }

        fun getWindowJsonList(context: Context, file: String): List<WindowKind> {
            val raw = getRawFile(file)
            val resources = context.resources
            val inputStream = resources.openRawResource(raw)

            inputStream.bufferedReader().use {
                val json = it.readText()
                val comodos = fromJson<List<WindowKind>>(json)
                return comodos
            }
        }

        fun getConstantes(context: Context, file: String): List<Constantes> {
            val raw = getRawFile(file)
            val resources = context.resources
            val inputStream = resources.openRawResource(raw)

            inputStream.bufferedReader().use {
                val json = it.readText()
                val cons = fromJson<List<Constantes>>(json)
                return cons
            }
        }


        private fun getRawFile(file: String) = when (file) {
            CLOSET -> R.raw.tipo_armario
            ROOM -> R.raw.tipo_comodo
            CONSTANTS -> R.raw.constantes
            MIRROR -> R.raw.tipo_espelho
            WINDOW -> R.raw.tipo_janela
            DOOR -> R.raw.tipo_jporta
            else -> R.raw.tipo_armario
        }

        inline fun <reified T> fromJson(json: String): T {
            val type = object : TypeToken<T>() {}.type
            return Gson().fromJson(json, type)
        }
    }
}
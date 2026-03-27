package com.jomar.senhorpintor.extensions

import android.os.Parcel
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

fun getCurrentDateString(): String? {
    var result = ""
    val d = Date()
    val sf = SimpleDateFormat("dd/MM/yyyy")
    result = sf.format(d)
    return result
}

fun getCurrentDateDate(): Date? {
    val d = Date()
    return Date(d.time)
}

fun getYearMonthDay(): String? {
    var result = ""
    val c = Calendar.getInstance()
    val ano = c[Calendar.YEAR].toString()
    val mesInt = c[Calendar.MONTH] + 1
    val mes: String
    mes = if (mesInt < 10) {
        "0$mesInt"
    } else {
        mesInt.toString()
    }
    val diaInt = c[Calendar.DAY_OF_MONTH]
    val dia: String
    dia = if (diaInt < 10) {
        "0$diaInt"
    } else {
        diaInt.toString()
    }
    val hora = c[Calendar.HOUR_OF_DAY].toString()
    val minuto = c[Calendar.MINUTE].toString()
    result = ano + mes + dia + hora + minuto
    return result
}

fun Parcel.writeDate(date: Date?) {
    writeLong(date?.time ?: -1)
}

fun Parcel.readDate(): Date? {
    val long = readLong()
    return if (long != -1L) Date(long) else null
}
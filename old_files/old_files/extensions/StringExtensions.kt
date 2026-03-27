package com.jomar.senhorpintor.extensions

fun String.toMonth() = when (this) {
    "01", "1" -> "Janeiro"
    "02", "2" -> "Fevereiro"
    "03", "3" -> "Março"
    "04", "4" -> "Abril"
    "05", "5" -> "Maio"
    "06", "6" -> "Junho"
    "07", "7" -> "Julho"
    "08", "8" -> "Agosto"
    "09", "9" -> "Setembro"
    "10" -> "Outubro"
    "11" -> "Novembro"
    else -> "Dezembro"
}

fun String.toMonthInitials() = when (this) {
    "01", "1" -> "Jan"
    "02", "2" -> "Fev"
    "03", "3" -> "Mar"
    "04", "4" -> "Abr"
    "05", "5" -> "Mai"
    "06", "6" -> "Jun"
    "07", "7" -> "Jul"
    "08", "8" -> "Ago"
    "09", "9" -> "Set"
    "10" -> "Out"
    "11" -> "Nov"
    else -> "Dez"
}

fun String.getPodcasDay() = split("-")[2]

fun String.getPodcasMonth() = split("-")[1]

fun String.getPodcastYear() = split("-")[0]

fun String.removeTimeFormat() = split("T")[0]
package br.com.orcacor.util

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.round

fun formatFloat(value: Float, decimals: Int = 2): String {
    val multiplier = 10.0.pow(decimals)
    val rounded = round(value.toDouble() * multiplier) / multiplier
    val intPart = rounded.toLong()
    val fracStr = abs((rounded - intPart) * multiplier).toLong().toString().padStart(decimals, '0')
    return "$intPart.$fracStr"
}
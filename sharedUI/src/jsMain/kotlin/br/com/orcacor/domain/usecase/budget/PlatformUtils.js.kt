package br.com.orcacor.domain.usecase.budget

import kotlin.js.Date
import kotlin.random.Random

actual fun randomUuid(): String {
    val chars = "0123456789abcdef"
    return buildString {
        repeat(32) { i ->
            if (i in listOf(8, 12, 16, 20)) append('-')
            append(chars[Random.nextInt(chars.length)])
        }
    }
}

actual fun currentTimeMillis(): Long = Date().getTime().toLong()

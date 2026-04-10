package br.com.orcacor.domain.usecase.budget

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

@JsFun("() => Date.now()")
private external fun jsDateNow(): Double

actual fun currentTimeMillis(): Long = jsDateNow().toLong()

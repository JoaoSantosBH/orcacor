package br.com.orcacor.domain.usecase.budget

import java.util.UUID

actual fun randomUuid(): String = UUID.randomUUID().toString()
actual fun currentTimeMillis(): Long = System.currentTimeMillis()

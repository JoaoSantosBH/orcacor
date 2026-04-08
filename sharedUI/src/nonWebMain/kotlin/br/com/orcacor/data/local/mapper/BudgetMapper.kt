package br.com.orcacor.data.local.mapper

import br.com.orcacor.data.local.entity.BudgetEntity
import br.com.orcacor.domain.entity.Budget
import br.com.orcacor.domain.entity.BudgetStatus

fun BudgetEntity.toDomain(rooms: List<br.com.orcacor.domain.entity.Room> = emptyList()): Budget =
    Budget(
        id = id,
        number = number,
        dateEpochMillis = dateEpochMillis,
        recipientName = recipientName,
        recipientEmail = recipientEmail,
        rooms = rooms,
        totalArea = totalArea,
        status = BudgetStatus.valueOf(status)
    )

fun Budget.toEntity(): BudgetEntity =
    BudgetEntity(
        id = id,
        number = number,
        dateEpochMillis = dateEpochMillis,
        recipientName = recipientName,
        recipientEmail = recipientEmail,
        status = status.name,
        totalArea = totalArea
    )

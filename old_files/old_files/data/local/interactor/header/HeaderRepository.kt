package com.jomar.senhorpintor.data.local.interactor.header

import com.jomar.senhorpintor.model.entities.BudgeReportHeader

interface HeaderRepository {
    suspend fun insertHeader(header: BudgeReportHeader): Long
    suspend fun updateHeader(header: BudgeReportHeader)
    suspend fun deleteHeader(header: BudgeReportHeader)
    suspend fun getHeader(id: Long): BudgeReportHeader
    suspend fun getAll(): List<BudgeReportHeader>
    suspend fun getHeaderFromBudgetNumber(number: String): BudgeReportHeader
}
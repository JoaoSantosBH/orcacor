package com.jomar.senhorpintor.data.local.interactor.header

import com.jomar.senhorpintor.model.entities.BudgeReportHeader

class HeaderInteractorImpl(private val repository: HeaderRepository): HeaderInteractor {
    override suspend fun insertHeader(header: BudgeReportHeader): Long {
        return repository.insertHeader(header)
    }

    override suspend fun updateHeader(header: BudgeReportHeader) {
        repository.updateHeader(header)
    }

    override suspend fun deleteHeader(header: BudgeReportHeader) {
        repository.deleteHeader(header)
    }

    override suspend fun getHeader(id: Long): BudgeReportHeader {
        return repository.getHeader(id)
    }

    override suspend fun getAll(): List<BudgeReportHeader> {
        return repository.getAll()
    }

    override suspend fun getHeaderFromBudgetNumber(number: String): BudgeReportHeader {
        return repository.getHeaderFromBudgetNumber(number)
    }
}
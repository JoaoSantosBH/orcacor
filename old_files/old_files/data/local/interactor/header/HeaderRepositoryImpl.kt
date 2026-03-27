package com.jomar.senhorpintor.data.local.interactor.header

import android.content.Context
import com.jomar.senhorpintor.dao.header.BudgetReportHeaderDao
import com.jomar.senhorpintor.model.entities.BudgeReportHeader

class HeaderRepositoryImpl(private val context: Context, private val dao: BudgetReportHeaderDao) : HeaderRepository {
    override suspend fun insertHeader(header: BudgeReportHeader): Long {
        return dao.insertHeader(header)
    }

    override suspend fun updateHeader(header: BudgeReportHeader) {
        dao.updateHeader(header)
    }

    override suspend fun deleteHeader(header: BudgeReportHeader) {
        dao.deleteHeader(header)
    }

    override suspend fun getHeader(id: Long): BudgeReportHeader {
        return dao.getHeader(id)
    }

    override suspend fun getAll(): List<BudgeReportHeader> {
        return dao.getAll()
    }

    override suspend fun getHeaderFromBudgetNumber(number: String): BudgeReportHeader {
        return dao.getHeaderFromBudgetNumber(number)
    }
}
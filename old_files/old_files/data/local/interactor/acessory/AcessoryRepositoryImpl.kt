package com.jomar.senhorpintor.data.local.interactor.acessory

import android.content.Context
import com.jomar.senhorpintor.dao.acessories.BudgetReportAcessoriesDao
import com.jomar.senhorpintor.model.entities.BudgetReportAcessories

class AcessoryRepositoryImpl(private val context: Context, private val dao: BudgetReportAcessoriesDao) : AcessoryRepository {
    override suspend fun insertAcessory(acessorie: BudgetReportAcessories): Long {
        return dao.insertAcessory(acessorie)
    }

    override suspend fun updateAcessory(acessorie: BudgetReportAcessories) {
        dao.updateAcessory(acessorie)
    }

    override suspend fun deleteAcessory(acessorie: BudgetReportAcessories) {
        dao.deleteAcessory(acessorie)
    }

    override suspend fun getAcessoriy(id: Long): BudgetReportAcessories {
        return dao.getAcessories(id)
    }

    override suspend fun getAAll(): List<BudgetReportAcessories> {
        return dao.getAll()
    }
}
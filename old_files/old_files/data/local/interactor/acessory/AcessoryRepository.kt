package com.jomar.senhorpintor.data.local.interactor.acessory

import com.jomar.senhorpintor.model.entities.BudgetReportAcessories

interface AcessoryRepository {
    suspend fun insertAcessory(acessorie: BudgetReportAcessories): Long
    suspend  fun updateAcessory(acessorie: BudgetReportAcessories)
    suspend fun deleteAcessory(acessorie: BudgetReportAcessories)
    suspend fun getAcessoriy(id: Long): BudgetReportAcessories
    suspend  fun getAAll(): List<BudgetReportAcessories>
}
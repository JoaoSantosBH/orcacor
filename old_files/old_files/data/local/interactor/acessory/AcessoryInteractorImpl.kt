package com.jomar.senhorpintor.data.local.interactor.acessory

import com.jomar.senhorpintor.model.entities.BudgetReportAcessories

class AcessoryInteractorImpl(private val repository: AcessoryRepository): AcessoryInteractor {
    override suspend fun insertAcessory(acessorie: BudgetReportAcessories): Long {
        return repository.insertAcessory(acessorie)
    }

    override suspend fun updateAcessory(acessorie: BudgetReportAcessories) {
        repository.updateAcessory(acessorie)
    }

    override suspend fun deleteAcessory(acessorie: BudgetReportAcessories) {
        repository.deleteAcessory(acessorie)
    }

    override suspend fun getAcessoriy(id: Long): BudgetReportAcessories {
        return repository.getAcessoriy(id)
    }

    override suspend fun getAAll(): List<BudgetReportAcessories> {
        return repository.getAAll()
    }
}
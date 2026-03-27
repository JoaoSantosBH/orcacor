package com.jomar.senhorpintor.data.local.interactor.budget

import com.jomar.senhorpintor.model.entities.Budget

class BudgetInteractorImpl(private val budgetRepository: BudgetRepository) : BudgetInteractor {
    override suspend fun insertBudget(budget: Budget) : Long{
        return budgetRepository.insertBudget(budget)
    }

    override suspend fun updateBudget(budget: Budget) {
        budgetRepository.updateBudget(budget)
    }
    override suspend fun deleteBudget(budget: Budget) {
        budgetRepository.deleteBudget(budget)
    }
    override suspend fun getBudget(id: Long): Budget {
        val budget = budgetRepository.getBudget(id)
        return budget
    }

    override suspend fun getBudgets(): MutableList<Budget>{
        val list = budgetRepository.getBudgets()
        return list
    }
}

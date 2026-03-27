package com.jomar.senhorpintor.data.local.interactor.budget

import com.jomar.senhorpintor.model.entities.Budget

interface BudgetInteractor {
        suspend fun insertBudget(budget: Budget): Long
        suspend fun updateBudget(budget: Budget)
        suspend fun deleteBudget(budget: Budget)
        suspend fun getBudget(id: Long): Budget
        suspend fun getBudgets(): MutableList<Budget>
}
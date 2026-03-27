package com.jomar.senhorpintor.data.local.interactor.budget

import android.content.Context
import com.jomar.senhorpintor.dao.budget.BudgetDao
import com.jomar.senhorpintor.model.entities.Budget

class BudgetRepositoryImpl(private val context: Context, private val budgetDao: BudgetDao) : BudgetRepository {

    override suspend fun insertBudget(budget: Budget): Long {
        return budgetDao.insertBudget(budget)
    }
    override suspend fun updateBudget(budget: Budget) {
        budgetDao.updateBudget(budget)
    }
    override suspend fun deleteBudget(budget: Budget) {
        budgetDao.deleteBudget(budget)
    }
    override suspend fun getBudget(id: Long): Budget {
        val budget = budgetDao.getBudget(id)
        return budget
    }

    override suspend fun getBudgets(): MutableList<Budget>{
        val list = budgetDao.getBudgets()
        return list
    }

}
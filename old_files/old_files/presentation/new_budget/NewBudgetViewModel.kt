package com.jomar.senhorpintor.presentation.new_budget

import android.app.Application
import com.jomar.senhorpintor.base.BaseViewModel
import com.jomar.senhorpintor.data.local.interactor.budget.BudgetInteractor
import com.jomar.senhorpintor.extensions.getCurrentDateDate
import com.jomar.senhorpintor.extensions.getYearMonthDay
import com.jomar.senhorpintor.model.entities.Budget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NewBudgetViewModel(app: Application, private val interactor: BudgetInteractor) : BaseViewModel(app) {

    var orderId: Long = 0
    lateinit var budget: Budget

    suspend fun insert(budget: Budget) = withContext(Dispatchers.IO) {
        val number = getYearMonthDay()
        val date = getCurrentDateDate()
        val persist = Budget(
                null,
                number,
                date,
                budget.badgeDestName,
                budget.badgeDestEmail)
        orderId = interactor.insertBudget(persist)
    }

    suspend fun getBudget(id: Long) = withContext(Dispatchers.IO) {
        budget = interactor.getBudget(id)
    }

    fun updateBudget(budget: Budget) = GlobalScope.launch {
        interactor.updateBudget(budget)
    }

    suspend fun deleteBudget(budget: Budget) = withContext(Dispatchers.IO) {
        interactor.deleteBudget(budget)
    }

}


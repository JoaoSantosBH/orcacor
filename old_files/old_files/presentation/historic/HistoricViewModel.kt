package com.jomar.senhorpintor.presentation.historic

import android.app.Application
import com.jomar.senhorpintor.base.BaseViewModel
import com.jomar.senhorpintor.data.local.interactor.budget.BudgetInteractor
import com.jomar.senhorpintor.data.local.interactor.rooms.RoomInteractor
import com.jomar.senhorpintor.model.entities.Budget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HistoricViewModel(app: Application, private val interactor: BudgetInteractor,private val interactorR: RoomInteractor) : BaseViewModel(app) {

    var orderId: Long = 0
    lateinit var budget: Budget
    lateinit var list: MutableList<Budget>

    suspend fun getBudget(id: Long) = withContext(Dispatchers.IO) {
        budget = interactor.getBudget(id)
    }

    fun updateBudget(budget: Budget) = GlobalScope.launch {
        interactor.updateBudget(budget)
    }

    fun deleteBudget(budget: Budget) = GlobalScope.launch {
        interactor.deleteBudget(budget)
    }

    suspend fun getBudgets() = withContext(Dispatchers.IO) {
       list = interactor.getBudgets()
    }

    suspend fun deleteBudgetAndRooms(budget: Budget) = withContext(Dispatchers.IO){
        //Delete Budget
        interactor.deleteBudget(budget)
        //Delete Rooms
        interactorR.deleteBudgetRooms(budget.badgeNumber!!)
    }

}


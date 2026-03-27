package com.jomar.senhorpintor.dao.budget

import androidx.room.*
import com.jomar.senhorpintor.model.entities.Budget

@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBudget(budget: Budget): Long

    @Update
    fun updateBudget(budget: Budget)

    @Delete
    fun deleteBudget(budget: Budget)

    @Query("SELECT * FROM Budget WHERE id = :id" )
    fun getBudget(id: Long): Budget

    @Query("SELECT * FROM Budget")
    fun getBudgets(): MutableList<Budget>
}
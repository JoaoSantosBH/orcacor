package com.jomar.senhorpintor.dao.acessories

import androidx.room.*
import com.jomar.senhorpintor.model.entities.BudgetReportAcessories

@Dao
interface BudgetReportAcessoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAcessory(acessorie: BudgetReportAcessories): Long

    @Update
    fun updateAcessory(acessorie: BudgetReportAcessories)

    @Delete
    fun deleteAcessory(acessorie: BudgetReportAcessories)

    @Query("SELECT * FROM BudgetReportAcessories WHERE id = :id" )
    fun getAcessories(id: Long): BudgetReportAcessories

    @Query("SELECT * FROM BudgetReportAcessories")
    fun getAll(): List<BudgetReportAcessories>
}
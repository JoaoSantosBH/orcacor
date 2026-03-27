package com.jomar.senhorpintor.dao.header

import androidx.room.*
import com.jomar.senhorpintor.model.entities.BudgeReportHeader

@Dao
interface BudgetReportHeaderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHeader(header: BudgeReportHeader): Long

    @Update
    fun updateHeader(header: BudgeReportHeader)

    @Delete
    fun deleteHeader(header: BudgeReportHeader)

    @Query("SELECT * FROM BudgeReportHeader WHERE id = :id")
    fun getHeader(id: Long): BudgeReportHeader

    @Query("SELECT * FROM BudgeReportHeader")
    fun getAll(): List<BudgeReportHeader>

    @Query("SELECT * FROM BudgeReportHeader WHERE badgeNumber = :number")
    fun getHeaderFromBudgetNumber(number: String): BudgeReportHeader
}
package com.jomar.senhorpintor.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jomar.senhorpintor.dao.acessories.BudgetReportAcessoriesDao
import com.jomar.senhorpintor.dao.budget.BudgetDao
import com.jomar.senhorpintor.dao.header.BudgetReportHeaderDao
import com.jomar.senhorpintor.dao.material.BudgetReportMaterialDao
import com.jomar.senhorpintor.dao.room.RoomDao
import com.jomar.senhorpintor.dao.user.UserDao
import com.jomar.senhorpintor.model.entities.*

@Database(entities = [User::class, Budget::class, Room::class, BudgeReportHeader::class, BudgetReportAcessories::class, BudgetReportMaterial::class], version = 4, exportSchema = true)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun badgeDao(): BudgetDao
    abstract fun roomDao(): RoomDao
    abstract fun headerDao(): BudgetReportHeaderDao
    abstract fun materialDao(): BudgetReportMaterialDao
    abstract fun acessoriesDao(): BudgetReportAcessoriesDao


}
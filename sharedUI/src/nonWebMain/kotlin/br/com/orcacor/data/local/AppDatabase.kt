package br.com.orcacor.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import br.com.orcacor.data.local.dao.AccessoryDao
import br.com.orcacor.data.local.dao.BudgetDao
import br.com.orcacor.data.local.dao.RoomDao
import br.com.orcacor.data.local.dao.UserDao
import br.com.orcacor.data.local.entity.AccessoryEntity
import br.com.orcacor.data.local.entity.BudgetEntity
import br.com.orcacor.data.local.entity.RoomEntity
import br.com.orcacor.data.local.entity.UserEntity

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

@Database(
    entities = [
        BudgetEntity::class,
        RoomEntity::class,
        AccessoryEntity::class,
        UserEntity::class
    ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    abstract fun roomDao(): RoomDao
    abstract fun accessoryDao(): AccessoryDao
    abstract fun userDao(): UserDao
}

const val DATABASE_NAME = "orcacor.db"

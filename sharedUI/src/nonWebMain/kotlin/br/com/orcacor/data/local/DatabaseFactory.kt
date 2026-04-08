package br.com.orcacor.data.local

import androidx.room.RoomDatabase

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>

fun createDatabase(): AppDatabase =
    getDatabaseBuilder()
        .fallbackToDestructiveMigration(true)
        .build()

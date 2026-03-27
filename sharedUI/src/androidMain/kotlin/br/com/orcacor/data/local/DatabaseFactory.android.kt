package br.com.orcacor.data.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

lateinit var androidContext: Context

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val dbFile = androidContext.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder<AppDatabase>(
        context = androidContext,
        name = dbFile.absolutePath
    )
}

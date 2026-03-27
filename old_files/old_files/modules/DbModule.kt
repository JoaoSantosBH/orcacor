package com.jomar.senhorpintor.modules

import androidx.room.Room
import com.jomar.senhorpintor.data.AppDatabase
import com.jomar.senhorpintor.data.room_migrations.RoomMigrations.Companion.MIGRATION_2_3
import com.jomar.senhorpintor.data.room_migrations.RoomMigrations.Companion.MIGRATION_3_4
import org.koin.dsl.module.module


val dbModule = module {
    single {
        Room.databaseBuilder(
                get(),
                AppDatabase::class.java,
                "senhor-database")
                .addMigrations(MIGRATION_2_3,MIGRATION_3_4)
                .build()
    }

    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().badgeDao() }
    single { get<AppDatabase>().roomDao() }
    single { get<AppDatabase>().headerDao() }
    single { get<AppDatabase>().materialDao() }
    single { get<AppDatabase>().acessoriesDao() }
}



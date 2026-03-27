package com.jomar.senhorpintor.data.room_migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

open class RoomMigrations {
    companion object {
        open val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `Budget` (" +
                        "`id` INTEGER , " +
                        "`num_orcamento` TEXT ," +
                        " `data_orcamento` INTEGER , " +
                        "`nome_destinatario` TEXT , " +
                        "`email_destinatario` TEXT ," +
                        " PRIMARY KEY(`id`))")
            }
        }

        open val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `Room` (" +
                        "`id` INTEGER , " +
                        "`idRoom` INTEGER ," +
                        " `orderNumber` TEXT , " +
                        "`kind` INTEGER , " +
                        " `note` TEXT , " +
                        "`wallIsNew` INTEGER ," +
                        "`doorNumber` INTEGER , " +
                        "`doorKind` INTEGER , " +
                        "`windowNumber` INTEGER , " +
                        "`windowKind` INTEGER , " +
                        "`closetNumber` INTEGER , " +
                        "`closetKind` INTEGER , " +
                        "`mirrorNumber` INTEGER , " +
                        "`mirrorKind` INTEGER , " +
                        "`demaos` INTEGER , " +
                        "`width` REAL , " +
                        "`height` REAL , " +
                        "`lenght` REAL , " +
                        "`teto` REAL , " +
                        "`base` REAL , " +
                        "`totalSquareMETER` REAL , " +
                        " PRIMARY KEY(`id`))")
            }
        }
        open val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                        "CREATE TABLE `BudgeReportHeader` (" +
                                "`id` INTEGER , " +
                                "`badgeNumber` TEXT , " +
                                "`painterName` TEXT , " +
                                "`painterCelPhone` TEXT , " +
                                "`clientName` TEXT , " +
                                "`clientEmail` TEXT , " +
                                "`date` INTEGER , " +
                                "`totalArea` REAL , " +
                                " PRIMARY KEY(`id`))"
                )
                database.execSQL(
                        "CREATE TABLE `BudgetReportAcessories` (" +
                                "`id` INTEGER , " +
                                "`budgetNumber` TEXT , " +
                                "`info` TEXT , " +
                                " PRIMARY KEY(`id`))"
                )
                database.execSQL(
                        "CREATE TABLE `BudgetReportMaterial` (" +
                                "`id` INTEGER , " +
                                "`budgetNumber` TEXT , " +
                                "`materialName` TEXT , " +
                                "`totalArea` REAL , " +
                                "`yeld` REAL , " +
                                "`info` TEXT , " +
                                "`diff` REAL , " +
                                "`totalLiter` REAL , " +
                                " PRIMARY KEY(`id`))"
                )
            }
        }

    }
}





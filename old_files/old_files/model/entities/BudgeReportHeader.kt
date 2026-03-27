package com.jomar.senhorpintor.model.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date
@Keep
@Entity
class BudgeReportHeader(
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null,
        var badgeNumber: String? = null,
        var painterName: String? = null,
        var painterCelPhone: String? = null,
        var clientName: String? = null,
        var clientEmail: String? = null,
        var date: Date? = null,
        var totalArea: Float? = null
)
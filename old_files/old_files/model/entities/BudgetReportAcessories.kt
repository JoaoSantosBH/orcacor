package com.jomar.senhorpintor.model.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity
data class BudgetReportAcessories(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        var budgetNumber: String? = null,
        var info: String? = null
)
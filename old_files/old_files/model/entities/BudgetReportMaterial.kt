package com.jomar.senhorpintor.model.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity
class BudgetReportMaterial(
        @PrimaryKey(autoGenerate = true)
        val id: Int?,
        val budgetNumber: String? = null,
        val materialName: String? = null,
        val totalArea: Float? = null,
        val yeld: Float? = null,
        val info: String? = null,
        val diff: Float? = null,
        val totalLiter: Float?
)
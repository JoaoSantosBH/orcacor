package br.com.orcacor.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey val id: String,
    val number: String,
    val dateEpochMillis: Long,
    val recipientName: String,
    val recipientEmail: String,
    val status: String,
    val totalArea: Float = 0f
)

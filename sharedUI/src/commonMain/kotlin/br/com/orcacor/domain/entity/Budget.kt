package br.com.orcacor.domain.entity

data class Budget(
    val id: String,
    val number: String,
    val dateEpochMillis: Long,
    val recipientName: String,
    val recipientEmail: String,
    val rooms: List<Room> = emptyList(),
    val totalArea: Float = 0f,
    val status: BudgetStatus = BudgetStatus.DRAFT
)

enum class BudgetStatus { DRAFT, SENT, ARCHIVED }

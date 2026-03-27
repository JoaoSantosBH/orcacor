package br.com.orcacor.domain.usecase.budget

import br.com.orcacor.domain.entity.Budget
import br.com.orcacor.domain.entity.BudgetStatus
import br.com.orcacor.domain.repository.BudgetRepository

class CreateBudgetUseCase(private val repository: BudgetRepository) {

    suspend fun invoke(recipientName: String, recipientEmail: String): Result<Budget> {
        return runCatching {
            val id = generateId()
            val number = generateNumber()
            val budget = Budget(
                id = id,
                number = number,
                dateEpochMillis = currentTimeMillis(),
                recipientName = recipientName,
                recipientEmail = recipientEmail,
                status = BudgetStatus.DRAFT
            )
            repository.save(budget)
        }
    }

    private fun generateId(): String = randomUuid()
    private fun generateNumber(): String {
        val ts = currentTimeMillis()
        return "ORC-${ts % 100_000}"
    }
}

expect fun randomUuid(): String
expect fun currentTimeMillis(): Long

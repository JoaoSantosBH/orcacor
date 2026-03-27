package br.com.orcacor.domain.usecase.budget

import br.com.orcacor.domain.repository.BudgetRepository
import br.com.orcacor.domain.repository.RoomRepository

class DeleteBudgetUseCase(
    private val budgetRepository: BudgetRepository,
    private val roomRepository: RoomRepository
) {
    suspend fun invoke(budgetId: String): Result<Unit> = runCatching {
        roomRepository.deleteByBudgetId(budgetId)
        budgetRepository.delete(budgetId)
    }
}

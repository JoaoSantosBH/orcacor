package br.com.orcacor.domain.usecase.budget

import br.com.orcacor.domain.entity.Budget
import br.com.orcacor.domain.repository.BudgetRepository

class SaveBudgetUseCase(private val repository: BudgetRepository) {

    suspend fun invoke(budget: Budget): Result<Budget> = runCatching {
        repository.save(budget)
    }
}

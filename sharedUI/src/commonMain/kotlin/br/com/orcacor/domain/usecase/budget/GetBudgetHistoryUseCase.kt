package br.com.orcacor.domain.usecase.budget

import br.com.orcacor.domain.entity.Budget
import br.com.orcacor.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow

class GetBudgetHistoryUseCase(private val repository: BudgetRepository) {

    fun invoke(): Flow<List<Budget>> = repository.getAll()
}

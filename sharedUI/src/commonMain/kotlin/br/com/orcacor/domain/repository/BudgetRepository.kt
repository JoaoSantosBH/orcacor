package br.com.orcacor.domain.repository

import br.com.orcacor.domain.entity.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getAll(): Flow<List<Budget>>
    suspend fun getById(id: String): Budget?
    suspend fun save(budget: Budget): Budget
    suspend fun delete(id: String)
}

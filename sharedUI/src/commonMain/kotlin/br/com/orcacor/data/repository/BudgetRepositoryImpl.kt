package br.com.orcacor.data.repository

import br.com.orcacor.data.local.dao.BudgetDao
import br.com.orcacor.data.local.dao.RoomDao
import br.com.orcacor.data.local.mapper.toDomain
import br.com.orcacor.data.local.mapper.toEntity
import br.com.orcacor.domain.entity.Budget
import br.com.orcacor.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetRepositoryImpl(
    private val budgetDao: BudgetDao,
    private val roomDao: RoomDao
) : BudgetRepository {

    override fun getAll(): Flow<List<Budget>> =
        budgetDao.getAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getById(id: String): Budget? =
        budgetDao.getById(id)?.toDomain()

    override suspend fun save(budget: Budget): Budget {
        budgetDao.upsert(budget.toEntity())
        return budget
    }

    override suspend fun delete(id: String) {
        budgetDao.deleteById(id)
    }
}

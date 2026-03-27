package br.com.orcacor.domain.usecase.report

import br.com.orcacor.domain.entity.Report
import br.com.orcacor.domain.repository.BudgetRepository
import br.com.orcacor.domain.repository.MaterialConstantsRepository
import br.com.orcacor.domain.repository.RoomRepository
import br.com.orcacor.domain.repository.UserRepository
import br.com.orcacor.domain.usecase.budget.currentTimeMillis
import br.com.orcacor.domain.usecase.material.CalculateMaterialsUseCase
import kotlinx.coroutines.flow.first

class GenerateReportUseCase(
    private val budgetRepository: BudgetRepository,
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    private val materialConstantsRepository: MaterialConstantsRepository,
    private val calculateMaterials: CalculateMaterialsUseCase
) {
    suspend fun invoke(budgetId: String): Result<Report> = runCatching {
        val budget = budgetRepository.getById(budgetId)
            ?: error("Orçamento não encontrado")
        val user = userRepository.getUser()
            ?: error("Usuário não encontrado")
        val rooms = roomRepository.getByBudgetId(budgetId).first()
        val constants = materialConstantsRepository.getConstants()
        val materials = calculateMaterials.invoke(rooms, constants)
        val totalArea = rooms.sumOf { it.totalSquareMeters.toDouble() }.toFloat()

        Report(
            budget = budget,
            user = user,
            rooms = rooms,
            materials = materials,
            totalArea = totalArea,
            generatedAtEpochMillis = currentTimeMillis()
        )
    }
}

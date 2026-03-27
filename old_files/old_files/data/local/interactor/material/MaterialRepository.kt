package com.jomar.senhorpintor.data.local.interactor.material

import com.jomar.senhorpintor.model.entities.BudgetReportMaterial

interface MaterialRepository {
    suspend fun insertMaterial(material: BudgetReportMaterial)
    suspend fun updateMaterial(material: BudgetReportMaterial)
    suspend fun deleteMaterial(material: BudgetReportMaterial)
    suspend fun getAll():List<BudgetReportMaterial>
}

package com.jomar.senhorpintor.data.local.interactor.material

import com.jomar.senhorpintor.model.entities.BudgetReportMaterial

class MaterialInteractorImpl(private val repository: MaterialRepository): MaterialInteractor {

    override suspend fun insertMaterial(material: BudgetReportMaterial){
        repository.insertMaterial(material)
    }
    override suspend fun updateMaterial(material: BudgetReportMaterial){
        repository.updateMaterial(material)
    }
    override suspend fun deleteMaterial(material: BudgetReportMaterial){
        repository.deleteMaterial(material)
    }
    override suspend fun getAll():List<BudgetReportMaterial>{
        return repository.getAll()
    }

}
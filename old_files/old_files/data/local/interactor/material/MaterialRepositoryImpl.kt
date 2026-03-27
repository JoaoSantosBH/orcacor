package com.jomar.senhorpintor.data.local.interactor.material

import android.content.Context
import com.jomar.senhorpintor.dao.material.BudgetReportMaterialDao
import com.jomar.senhorpintor.model.entities.BudgetReportMaterial

class MaterialRepositoryImpl(private val context: Context, private val materialDao: BudgetReportMaterialDao): MaterialRepository {
    override suspend fun insertMaterial(material: BudgetReportMaterial){
        materialDao.insertMaterial(material)
    }
    override suspend fun updateMaterial(material: BudgetReportMaterial){
        materialDao.updateMaterial(material)
    }
    override suspend fun deleteMaterial(material: BudgetReportMaterial){
        materialDao.deleteMaterial(material)
    }
    override suspend fun getAll():List<BudgetReportMaterial>{
       return materialDao.getMaterials()
    }
}
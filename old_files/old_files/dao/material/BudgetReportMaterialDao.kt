package com.jomar.senhorpintor.dao.material

import androidx.room.*
import com.jomar.senhorpintor.model.entities.BudgetReportMaterial
@Dao
interface BudgetReportMaterialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMaterial(material: BudgetReportMaterial): Long

    @Update
    fun updateMaterial(material: BudgetReportMaterial)

    @Delete
    fun deleteMaterial(material: BudgetReportMaterial)

    @Query("SELECT * FROM BudgetReportMaterial WHERE id = :id" )
    fun getMaterial(id: Long): BudgetReportMaterial

    @Query("SELECT * FROM BudgetReportMaterial")
    fun getMaterials(): List<BudgetReportMaterial>
}
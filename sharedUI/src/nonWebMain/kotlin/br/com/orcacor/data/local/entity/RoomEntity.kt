package br.com.orcacor.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "rooms",
    foreignKeys = [
        ForeignKey(
            entity = BudgetEntity::class,
            parentColumns = ["id"],
            childColumns = ["budgetId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("budgetId")]
)
data class RoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val budgetId: String,
    val name: String,
    val kind: String,
    val note: String?,
    val wallIsNew: Boolean,
    val desiredColor: String?,
    val width: Float?,
    val height: Float?,
    val length: Float?,
    val irregularWalls: String, // JSON encoded List<Float>
    val ceilingArea: Float,
    val wallsArea: Float,
    val totalSquareMeters: Float,
    val coats: Int
)

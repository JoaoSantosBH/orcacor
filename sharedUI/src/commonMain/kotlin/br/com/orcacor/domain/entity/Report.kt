package br.com.orcacor.domain.entity

data class Report(
    val budget: Budget,
    val user: User,
    val rooms: List<Room>,
    val materials: List<MaterialEstimate>,
    val totalArea: Float,
    val generatedAtEpochMillis: Long
)

data class MaterialEstimate(
    val name: String,
    val totalArea: Float,
    val yieldPerLiter: Float,
    val totalLiters: Float,
    val cans18L: Int,
    val cans36L: Int,
    val cans09L: Int,
    val remainder: Float
)

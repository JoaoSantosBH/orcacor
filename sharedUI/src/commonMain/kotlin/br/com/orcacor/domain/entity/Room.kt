package br.com.orcacor.domain.entity

data class Room(
    val id: Long? = null,
    val budgetId: String,
    val name: String,
    val kind: RoomKind,
    val note: String? = null,
    val wallIsNew: Boolean = false,
    val desiredColor: String? = null,
    val width: Float? = null,
    val height: Float? = null,
    val length: Float? = null,
    val irregularWalls: List<Float> = emptyList(),
    val doors: List<Accessory> = emptyList(),
    val windows: List<Accessory> = emptyList(),
    val mirrors: List<Accessory> = emptyList(),
    val closets: List<Accessory> = emptyList(),
    val photos: List<String> = emptyList(),
    val ceilingArea: Float = 0f,
    val wallsArea: Float = 0f,
    val totalSquareMeters: Float = 0f,
    val coats: Int = 2
)

enum class RoomKind { SYMMETRIC, ASYMMETRIC, EXTERNAL }

package br.com.orcacor.domain.entity

data class Accessory(
    val type: AccessoryType,
    val kindId: Int,
    val quantity: Int,
    val area: Float,
    val photos: List<String> = emptyList()
)

enum class AccessoryType { DOOR, WINDOW, MIRROR, CLOSET }

package br.com.orcacor.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class MaterialConstants(
    val sealer: Float = 0.1f,
    val plaster: Float = 0.5f,
    val paint: Float = 0.25f,
    val tow: Float = 0.05f,
    val maskingTape: Float = 0.1f,
    val sandpaper: Float = 0.1f,
    val wetSandpaper: Float = 0.05f,
    val plasticSheet: Float = 1.2f,
    val woolRoller: Float = 0.02f,
    val brush: Float = 0.02f
)

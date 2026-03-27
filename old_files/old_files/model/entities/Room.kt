package com.jomar.senhorpintor.model.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity
data class Room(
        @PrimaryKey(autoGenerate = true)
        val id: Long?,
        var idRoom: Int? = null,
        val orderNumber: String? = null,
        val kind: Int? = null,
        val note: String? = null,
        val wallIsNew: Boolean? = null,
        val doorNumber: Int? = null,
        val doorKind: Int? = null,
        val windowNumber: Int? = null,
        val windowKind: Int? = null,
        val closetNumber: Int? = null,
        val closetKind: Int? = null,
        val mirrorNumber: Int? = null,
        val mirrorKind: Int? = null,
        val demaos: Int? = null,
        val width: Float? = null,
        val height: Float? = null,
        val lenght: Float? = null,
        var teto: Float? = null,
        var base: Float? = null,
        var totalSquareMETER: Float? = null
)
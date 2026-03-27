package com.jomar.senhorpintor.model.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
@Keep
@Entity
data class User(
        @PrimaryKey(autoGenerate = true)
        val id: Int? = null,
        val nome: String? = null,
        val email: String? = null,
        val zapUsuario: String? = null
)
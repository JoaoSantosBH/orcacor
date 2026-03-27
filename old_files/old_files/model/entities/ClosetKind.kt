package com.jomar.senhorpintor.model.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Keep
@Entity
class ClosetKind(
        @PrimaryKey(autoGenerate = true)
        @SerializedName("id_tipo_armario")
        val id: Int,
        @SerializedName("nome_tipo_armario")
        val name: String,
        @SerializedName("area_tipo_armario")
        val area: Float
){
    override fun toString(): String {
        return name
    }
}


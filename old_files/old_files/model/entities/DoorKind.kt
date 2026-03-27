package com.jomar.senhorpintor.model.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Keep
@Entity
data class DoorKind(
        @PrimaryKey(autoGenerate = true)
        @SerializedName("id_tipo_porta")
        val id: Int,
        @SerializedName("nome_tipo_porta")
        val name: String,
        @SerializedName("area_tipo_porta")
        val area: Float
){
    override fun toString(): String {
        return name
    }
}

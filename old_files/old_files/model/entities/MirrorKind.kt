package com.jomar.senhorpintor.model.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Keep
@Entity
class MirrorKind(
        @PrimaryKey(autoGenerate = true)
        @SerializedName("id_tipo_espelho")
        val id: Int,
        @SerializedName("nome_tipo_espelho")
        val name: String,
        @SerializedName("area_tipo_espelho")
        val area: Float
){
    override fun toString(): String {
        return name
    }
}

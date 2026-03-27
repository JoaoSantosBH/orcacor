package com.jomar.senhorpintor.model.entities

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Keep
@Entity
data class WindowKind(
        @PrimaryKey(autoGenerate = true)
        @SerializedName("id_tipo_janela")
        val id: Int,
        @SerializedName("nome_tipo_janela")
        val name:String,
        @SerializedName("area_tipo_janela")
        val area: Float

){
    override fun toString(): String {
        return name
    }
}
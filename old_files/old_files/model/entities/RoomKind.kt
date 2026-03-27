package com.jomar.senhorpintor.model.entities


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class RoomKind(
        @SerializedName("id_tipo_comodo")
    val id: Int,
        @SerializedName("nome_tipo_comodo")
    val name: String,
        @SerializedName("localizacao_tipo_comodo")
    val localization: String
){
    override fun toString(): String {
        return name// What to display in the Spinner list.
    }
}


package com.jomar.senhorpintor.model.entities


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
@Keep
data class Constantes(
    @SerializedName("id_constantes")
    val idConstantes: Int,
    @SerializedName("estopam2")
    val estopam2: Float,
    @SerializedName("fitacrepem2")
    val fitacrepem2: Float,
    @SerializedName("lixa_qtym2")
    val lixaQtym2: Float,
    @SerializedName("lixaagua_qtym2")
    val lixaaguaQtym2: Float,
    @SerializedName("lona_plasticam2")
    val lonaPlasticam2: Float,
    @SerializedName("massa_litrom2")
    val massaLitrom2: Float,
    @SerializedName("rolo_lam2")
    val roloLam2: Float,
    @SerializedName("seladora_litrom2")
    val seladoraLitrom2: Float,
    @SerializedName("tinta_litrom2")
    val tintaLitrom2: Float,
    @SerializedName("trincham2")
    val trincham2: Float,
    @SerializedName("valor_hora_lixam2")
    val valorHoraLixam2: Float,
    @SerializedName("valor_hora_massam2")
    val valorHoraMassam2: Float,
    @SerializedName("valor_hora_pinturam2")
    val valorHoraPinturam2: Float
)
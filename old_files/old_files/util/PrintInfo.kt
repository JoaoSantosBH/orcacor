package com.jomar.senhorpintor.util

import android.content.Context
import com.jomar.senhorpintor.base.*
import com.jomar.senhorpintor.dto.AmountDTO
import com.jomar.senhorpintor.model.entities.Constantes

object PrintInfo {

    open fun printAmountInfo(areaSeladoraResult: Float, seladora: AmountDTO, context: Context): String {
        var info: String = ""
        if (areaSeladoraResult <= 0) {
            return info
        }
        if (seladora.lata18 > 0) {
            info += seladora.lata18.toString()+ " " + context.getString(LATAO) + "\n"
        }
        if (seladora.lata36 > 0) {
            info += seladora.lata36.toString()+ " "  + context.getString(GALAO) + "\n"
        }
        if (seladora.lata09 > 0) {
            info += seladora.lata09.toString() + context.getString(GALAOZINHO) + "\n"
        }
         String.format("%.2f", seladora.dif)+ " "  + context.getString(LITROS) + "\n"
        return info
    }


    fun imprimeListagemAcessorios(areatotal: Float, cons: Constantes,  context: Context): String? {
         var info: String = ""
        if (areatotal < 500) {
            var result = (cons.estopam2 * areatotal).toInt()
            if (result <= 0) {
                result = 1
            }
            info += result.toString() + " " + context.getString(ESTOPA) + "\n"
            result = (areatotal / cons.fitacrepem2).toInt()
            if (result <= 0) {
                result = 1
            }
            info += result.toString() + " " + context.getString(FITA_CREPE)+ "\n"
            result = (cons.lixaQtym2 * areatotal).toInt()
            if (result <= 0) {
                result = 1
            }
            info += result.toString() + " " + context.getString(LIXA)+ "\n"
            result = (cons.lixaaguaQtym2 * areatotal).toInt()
            if (result <= 0) {
                result = 1
            }
            info += result.toString() + " " + context.getString(LIXA_AGUA)+ "\n"
            result = (cons.lonaPlasticam2 * areatotal).toInt()
            if (result <= 0) {
                result = 1
            }
            info += result.toString() + " " + context.getString(LONA)+ "\n"
            result = (cons.roloLam2 * areatotal).toInt()
            if (result <= 0) {
                result = 1
            }
            info += result.toString() + " " + context.getString(ROLO_LA)+ "\n"
            result = (cons.trincham2 * areatotal).toInt()
            if (result <= 0) {
                result = 1
            }
            info += result.toString() + " " + context.getString(TRINCHA)+ "\n"
            return info
        }
        var result = (cons.estopam2 * areatotal).toInt()
        info += result.toString() + " " + context.getString(ESTOPA)+ "\n"
        result = (areatotal / cons.fitacrepem2).toInt()
        info += result.toString() + " " + context.getString(FITA_CREPE)+ "\n"
        result = (cons.lixaQtym2 * areatotal).toInt()
        info += result.toString() + " " + context.getString(LIXA)+ "\n"
        result = (cons.lixaaguaQtym2 * areatotal).toInt()
        info += result.toString() + " " + context.getString(LIXA_AGUA)+ "\n"
        result = (cons.lonaPlasticam2 * areatotal).toInt()
        info += result.toString() + " " + context.getString(LONA)+ "\n"
        result = (cons.roloLam2 * areatotal).toInt()
        info += result.toString() + " " + context.getString(ROLO_LA)+ "\n"
        result = (cons.trincham2 * areatotal).toInt()
        info += result.toString() + " " + context.getString(TRINCHA)+ "\n"
        info += context.getString(LINE)+ "\n"
        return info
    }

}


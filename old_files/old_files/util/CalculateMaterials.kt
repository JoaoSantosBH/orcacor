package com.jomar.senhorpintor.util

import com.jomar.senhorpintor.dto.AmountDTO

object CalculateMaterials {

    fun calculateAmountMaterialLiters(area: Float, value: Float): AmountDTO {
        val s = AmountDTO()
        val m2totalSeladora: Float
        var lata18Litros = 0
        s.lata18 = 0
        var lata36Litros = 0
        s.lata36 = 0
        var lata09Litros: Int
        var numLitrosSeladora: Float
        val RENDIMENTOPORLITROSELADORA = value
        m2totalSeladora = area
        numLitrosSeladora = m2totalSeladora / RENDIMENTOPORLITROSELADORA
        s.litros = numLitrosSeladora
        if (numLitrosSeladora >= 18) {
            lata18Litros = CalcularLatas.calculoLata18Lt(numLitrosSeladora)
            numLitrosSeladora -= lata18Litros * 18.toFloat()
            s.lata18 = lata18Litros
        }
        if (numLitrosSeladora >= 3.6) {
            lata36Litros = CalcularLatas.calculoLata36Lt(numLitrosSeladora)
            s.lata36 = lata36Litros
        }
        numLitrosSeladora -= lata36Litros * 3.6f
        lata09Litros = CalcularLatas.calculoLata09Lt(numLitrosSeladora)
        numLitrosSeladora -= lata09Litros * .9f
        s.lata09 = lata09Litros
        if (numLitrosSeladora < 0) {
            numLitrosSeladora = numLitrosSeladora * -1
        }
        if (numLitrosSeladora >= .9) {
            lata09Litros++
            s.lata09 = lata09Litros
            numLitrosSeladora -= lata09Litros.toFloat()
        }
        s.dif = numLitrosSeladora
        return s
    }
}
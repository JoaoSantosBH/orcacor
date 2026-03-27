package br.com.orcacor.domain.usecase.material

import br.com.orcacor.domain.entity.MaterialConstants
import br.com.orcacor.domain.entity.MaterialEstimate
import br.com.orcacor.domain.entity.Room

class CalculateMaterialsUseCase {

    fun invoke(rooms: List<Room>, constants: MaterialConstants): List<MaterialEstimate> {
        val totalArea = rooms.sumOf { it.totalSquareMeters.toDouble() }.toFloat()
        val totalAreaWithCoats = rooms.sumOf { (it.totalSquareMeters * it.coats).toDouble() }.toFloat()
        val hasNewWalls = rooms.any { it.wallIsNew }

        val estimates = mutableListOf<MaterialEstimate>()

        if (hasNewWalls) {
            val newWallArea = rooms
                .filter { it.wallIsNew }
                .sumOf { (it.totalSquareMeters * it.coats).toDouble() }
                .toFloat()

            estimates += calculateLiters("Seladora", newWallArea, constants.sealer)
            estimates += calculateLiters("Massa Corrida", newWallArea, constants.plaster)
        }

        estimates += calculateLiters("Tinta", totalAreaWithCoats, constants.paint)
        estimates += calculateQuantity("Estopa", totalArea, constants.tow)
        estimates += calculateQuantity("Fita Crepe", totalArea, constants.maskingTape)
        estimates += calculateQuantity("Lixa Seca", totalArea, constants.sandpaper)
        estimates += calculateQuantity("Lixa D'água", totalArea, constants.wetSandpaper)
        estimates += calculateQuantity("Lona Plástica", totalArea, constants.plasticSheet)
        estimates += calculateQuantity("Rolo de Lã", totalArea, constants.woolRoller)
        estimates += calculateQuantity("Trincha", totalArea, constants.brush)

        return estimates
    }

    private fun calculateLiters(name: String, area: Float, yieldPerLiter: Float): MaterialEstimate {
        if (yieldPerLiter <= 0f) return emptyEstimate(name, area, yieldPerLiter)

        var liters = area / yieldPerLiter
        val cans18 = if (liters >= 18f) (liters / 18f).toInt() else 0
        liters -= cans18 * 18f
        val cans36 = if (liters >= 3.6f) (liters / 3.6f).toInt() else 0
        liters -= cans36 * 3.6f
        var cans09 = (liters / 0.9f).toInt()
        liters -= cans09 * 0.9f
        if (liters < 0f) liters = -liters
        if (liters >= 0.9f) {
            cans09++
            liters -= 0.9f
        }

        return MaterialEstimate(
            name = name,
            totalArea = area,
            yieldPerLiter = yieldPerLiter,
            totalLiters = area / yieldPerLiter,
            cans18L = cans18,
            cans36L = cans36,
            cans09L = cans09,
            remainder = liters
        )
    }

    private fun calculateQuantity(name: String, area: Float, ratePerM2: Float): MaterialEstimate {
        val qty = area * ratePerM2
        return MaterialEstimate(
            name = name,
            totalArea = area,
            yieldPerLiter = ratePerM2,
            totalLiters = qty,
            cans18L = 0,
            cans36L = 0,
            cans09L = kotlin.math.ceil(qty.toDouble()).toInt(),
            remainder = 0f
        )
    }

    private fun emptyEstimate(name: String, area: Float, rate: Float) = MaterialEstimate(
        name = name,
        totalArea = area,
        yieldPerLiter = rate,
        totalLiters = 0f,
        cans18L = 0,
        cans36L = 0,
        cans09L = 0,
        remainder = 0f
    )
}

package br.com.orcacor.domain.usecase.room

import br.com.orcacor.domain.entity.Room
import br.com.orcacor.domain.entity.RoomKind

class CalculateRoomAreaUseCase {

    fun invoke(room: Room): Room {
        return when (room.kind) {
            RoomKind.SYMMETRIC -> calculateSymmetric(room)
            RoomKind.ASYMMETRIC -> calculateAsymmetric(room)
            RoomKind.EXTERNAL -> calculateExternal(room)
        }
    }

    private fun calculateSymmetric(room: Room): Room {
        val width = room.width ?: 0f
        val length = room.length ?: 0f
        val height = room.height ?: 0f

        //paredes = ((Lx2) * A) + ((Cx2) *A)
        val wallsArea = ((width * 2) * height) + ((length * 2) * height)

        //teto = L x C
        val ceilingArea = width * length

        //base = Paredes + teto
        val grossArea = wallsArea + ceilingArea

        //janelas portaas etc
        val discounts = totalDiscounts(room)

        //m2 = base - Janela - porta
        val totalSquareMeters = grossArea - discounts

        return room.copy(
            wallsArea = wallsArea,
            ceilingArea = ceilingArea,
            totalSquareMeters = maxOf(totalSquareMeters, 0f)
        )
    }





    //base= paredes - janelas - portas
    private fun calculateAsymmetric(room: Room): Room {
        val height = room.height ?: 0f
        val walls = room.irregularWalls

        //somatória = P1 + P2 + … PN
        val sum = walls.sum()

        //paredes = somatória * Altura
        val wallsArea = sum * height

        //media = somatoria / n
        val avg = if (walls.isNotEmpty()) sum / 4f else 0f

        //teto = media^2
        val ceilingArea = avg * avg

        val discounts = totalDiscounts(room)
        // Ordem correta: (paredes - descontos) + teto

        //m2 = base + teto
        val totalSquareMeters = (wallsArea - discounts) + ceilingArea

        return room.copy(
            wallsArea = wallsArea,
            ceilingArea = ceilingArea,
            totalSquareMeters = maxOf(totalSquareMeters, 0f)
        )
    }

    private fun calculateExternal(room: Room): Room {
        val width = room.width ?: 0f
        val height = room.height ?: 0f

        //base = (L* A)
        val wallsArea = width * height
        val discounts = room.windows.sumOf { it.area.toDouble() }.toFloat() +
                room.doors.sumOf { it.area.toDouble() }.toFloat()

        //m2 = base - Janela - porta
        val totalSquareMeters = wallsArea - discounts

        return room.copy(
            wallsArea = wallsArea,
            ceilingArea = 0f,
            totalSquareMeters = maxOf(totalSquareMeters, 0f)
        )
    }

    private fun totalDiscounts(room: Room): Float {
        val windows = room.windows.sumOf { it.area.toDouble() }.toFloat()
        val doors = room.doors.sumOf { it.area.toDouble() }.toFloat()
        val mirrors = room.mirrors.sumOf { it.area.toDouble() }.toFloat()
        val closets = room.closets.sumOf { it.area.toDouble() }.toFloat()
        return windows + doors + mirrors + closets
    }
}

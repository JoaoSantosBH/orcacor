package br.com.orcacor.data.local.mapper

import br.com.orcacor.data.local.entity.AccessoryEntity
import br.com.orcacor.data.local.entity.RoomEntity
import br.com.orcacor.domain.entity.Accessory
import br.com.orcacor.domain.entity.AccessoryType
import br.com.orcacor.domain.entity.Room
import br.com.orcacor.domain.entity.RoomKind
import kotlinx.serialization.json.Json

fun RoomEntity.toDomain(accessories: List<AccessoryEntity>): Room {
    val doors = accessories.filter { it.type == AccessoryType.DOOR.name }.map { it.toDomain() }
    val windows = accessories.filter { it.type == AccessoryType.WINDOW.name }.map { it.toDomain() }
    val mirrors = accessories.filter { it.type == AccessoryType.MIRROR.name }.map { it.toDomain() }
    val closets = accessories.filter { it.type == AccessoryType.CLOSET.name }.map { it.toDomain() }
    val walls = if (irregularWalls.isNotBlank())
        Json.decodeFromString<List<Float>>(irregularWalls) else emptyList()

    return Room(
        id = id,
        budgetId = budgetId,
        name = name,
        kind = RoomKind.valueOf(kind),
        note = note,
        wallIsNew = wallIsNew,
        desiredColor = desiredColor,
        width = width,
        height = height,
        length = length,
        irregularWalls = walls,
        doors = doors,
        windows = windows,
        mirrors = mirrors,
        closets = closets,
        ceilingArea = ceilingArea,
        wallsArea = wallsArea,
        totalSquareMeters = totalSquareMeters,
        coats = coats
    )
}

fun Room.toEntity(): RoomEntity =
    RoomEntity(
        id = id ?: 0L,
        budgetId = budgetId,
        name = name,
        kind = kind.name,
        note = note,
        wallIsNew = wallIsNew,
        desiredColor = desiredColor,
        width = width,
        height = height,
        length = length,
        irregularWalls = Json.encodeToString(irregularWalls),
        ceilingArea = ceilingArea,
        wallsArea = wallsArea,
        totalSquareMeters = totalSquareMeters,
        coats = coats
    )

fun Room.toAccessoryEntities(roomId: Long): List<AccessoryEntity> {
    return (doors.map { it.toEntity(roomId) } +
            windows.map { it.toEntity(roomId) } +
            mirrors.map { it.toEntity(roomId) } +
            closets.map { it.toEntity(roomId) })
}

fun AccessoryEntity.toDomain(): Accessory =
    Accessory(
        type = AccessoryType.valueOf(type),
        kindId = kindId,
        quantity = quantity,
        area = area,
        photos = if (photos.isNotBlank()) Json.decodeFromString(photos) else emptyList()
    )

fun Accessory.toEntity(roomId: Long): AccessoryEntity =
    AccessoryEntity(
        roomId = roomId,
        type = type.name,
        kindId = kindId,
        quantity = quantity,
        area = area,
        photos = Json.encodeToString(photos)
    )

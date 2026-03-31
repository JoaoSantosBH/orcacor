package br.com.orcacor.data.repository

import br.com.orcacor.domain.entity.ClosetKind
import br.com.orcacor.domain.entity.DoorKind
import br.com.orcacor.domain.entity.MaterialConstants
import br.com.orcacor.domain.entity.MirrorKind
import br.com.orcacor.domain.entity.WindowKind
import br.com.orcacor.domain.repository.MaterialConstantsRepository

class MaterialConstantsRepositoryImpl : MaterialConstantsRepository {

    override suspend fun getConstants(): MaterialConstants = defaultConstants

    override suspend fun getWindowKinds(): List<WindowKind> = windowKinds

    override suspend fun getDoorKinds(): List<DoorKind> = doorKinds

    override suspend fun getMirrorKinds(): List<MirrorKind> = mirrorKinds

    override suspend fun getClosetKinds(): List<ClosetKind> = closetKinds

    companion object {
        val defaultConstants = MaterialConstants(
            sealer = 10f,      // 10 m²/L
            plaster = 2f,      // 2 m²/L
            paint = 10f,       // 10 m²/L (rendimento padrão)
            tow = 0.05f,
            maskingTape = 0.1f,
            sandpaper = 0.1f,
            wetSandpaper = 0.05f,
            plasticSheet = 1.2f,
            woolRoller = 0.02f,
            brush = 0.02f
        )

        val windowKinds = listOf(
            WindowKind(1, "40 cm x 40 cm", 0.16f),
            WindowKind(2, "40 cm x 50 cm", 0.2f),
            WindowKind(3, "50 cm x 50 cm", 0.25f),
            WindowKind(4, "60 cm x 40 cm", 0.24f),
            WindowKind(5, "60 cm x 50 cm", 0.3f),
            WindowKind(6, "60 cm x 60 cm", 0.36f),
            WindowKind(7, "60 cm x 80 cm", 0.48f),
            WindowKind(8, "100 cm x 120 cm", 1.2f),
            WindowKind(9, "100 cm x 140 cm", 1.4f),
            WindowKind(10, "100 cm x 150 cm", 1.5f),
            WindowKind(11, "100 cm x 200 cm", 2.0f),
            WindowKind(12, "120 cm x 150 cm", 1.8f),
            WindowKind(13, "120 cm x 200 cm", 2.4f),
            WindowKind(14, "200 cm x 150 cm", 3.0f),
            WindowKind(15, "200 cm x 200 cm", 4.0f),
            WindowKind(16, "250 cm x 150 cm", 3.75f),
            WindowKind(17, "250 cm x 200 cm", 5.0f),
            WindowKind(18, "250 cm x 250 cm", 6.25f),
            WindowKind(19, "300 cm x 100 cm", 3.0f),
            WindowKind(20, "300 cm x 200 cm", 6.0f),
            WindowKind(21, "300 cm x 250 cm", 7.5f),
            WindowKind(22, "300 cm x 300 cm", 9.0f),
            WindowKind(23, "300 cm x 350 cm", 10.5f),
            WindowKind(24, "350 cm x 350 cm", 12.25f),
            WindowKind(25, "400 cm x 400 cm", 16.0f)
        )

        val doorKinds = listOf(
            DoorKind(1, "50 cm x 210 cm", 1.05f),
            DoorKind(2, "60 cm x 210 cm", 1.26f),
            DoorKind(3, "70 cm x 210 cm", 1.47f),
            DoorKind(4, "80 cm x 210 cm", 1.68f),
            DoorKind(5, "90 cm x 210 cm", 1.89f),
            DoorKind(6, "100 cm x 210 cm", 2.1f),
            DoorKind(7, "110 cm x 210 cm", 2.31f),
            DoorKind(8, "120 cm x 210 cm", 2.52f),
            DoorKind(9, "130 cm x 210 cm", 2.73f),
            DoorKind(10, "140 cm x 210 cm", 2.94f),
            DoorKind(11, "150 cm x 210 cm", 3.15f),
            DoorKind(12, "160 cm x 210 cm", 3.36f),
            DoorKind(13, "170 cm x 210 cm", 3.57f),
            DoorKind(14, "180 cm x 210 cm", 3.78f),
            DoorKind(15, "190 cm x 210 cm", 3.99f),
            DoorKind(16, "200 cm x 210 cm", 4.2f),
            DoorKind(17, "250 cm x 210 cm", 5.25f),
            DoorKind(18, "300 cm x 210 cm", 6.3f),
            DoorKind(19, "350 cm x 210 cm", 7.35f),
            DoorKind(20, "400 cm x 210 cm", 8.4f)
        )

        val mirrorKinds = listOf(
            MirrorKind(1, "40 cm x 40 cm", 0.16f),
            MirrorKind(2, "50 cm x 50 cm", 0.25f),
            MirrorKind(3, "80 cm x 80 cm", 0.64f),
            MirrorKind(4, "90 cm x 70 cm", 0.63f),
            MirrorKind(5, "100 cm x 100 cm", 1.0f),
            MirrorKind(6, "150 cm x 100 cm", 1.5f),
            MirrorKind(7, "150 cm x 150 cm", 2.25f)
        )

        val closetKinds = listOf(
            ClosetKind(1, "150 cm x 70 cm", 1.05f),
            ClosetKind(2, "220 cm x 150 cm", 3.3f),
            ClosetKind(3, "238 cm x 175 cm", 4.165f),
            ClosetKind(4, "240 cm x 275 cm", 6.6f),
            ClosetKind(5, "240 cm x 40 cm", 0.96f),
            ClosetKind(6, "275 cm x 275 cm", 7.56f)
        )
    }
}

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
            WindowKind(1, "Janela Simples (0,60 x 1,20m)", 0.72f),
            WindowKind(2, "Janela Média (1,20 x 1,20m)", 1.44f),
            WindowKind(3, "Janela Grande (1,80 x 1,20m)", 2.16f),
            WindowKind(4, "Janela Box (0,60 x 0,60m)", 0.36f),
            WindowKind(5, "Janela Panorâmica (2,40 x 1,50m)", 3.60f)
        )

        val doorKinds = listOf(
            DoorKind(1, "Porta Simples (0,80 x 2,10m)", 1.68f),
            DoorKind(2, "Porta Larga (0,90 x 2,10m)", 1.89f),
            DoorKind(3, "Porta Dupla (1,60 x 2,10m)", 3.36f),
            DoorKind(4, "Porta de Correr (2,00 x 2,10m)", 4.20f)
        )

        val mirrorKinds = listOf(
            MirrorKind(1, "Espelho Pequeno (0,50 x 0,70m)", 0.35f),
            MirrorKind(2, "Espelho Médio (0,80 x 1,00m)", 0.80f),
            MirrorKind(3, "Espelho Grande (1,20 x 1,50m)", 1.80f),
            MirrorKind(4, "Espelho de Corpo Inteiro (0,60 x 1,80m)", 1.08f)
        )

        val closetKinds = listOf(
            ClosetKind(1, "Armário Embutido Pequeno (1,00 x 2,10m)", 2.10f),
            ClosetKind(2, "Armário Embutido Médio (1,50 x 2,10m)", 3.15f),
            ClosetKind(3, "Armário Embutido Grande (2,00 x 2,10m)", 4.20f),
            ClosetKind(4, "Armário de Cozinha (3,00 x 0,90m)", 2.70f)
        )
    }
}

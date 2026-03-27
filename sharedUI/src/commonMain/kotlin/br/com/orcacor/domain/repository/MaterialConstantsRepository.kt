package br.com.orcacor.domain.repository

import br.com.orcacor.domain.entity.ClosetKind
import br.com.orcacor.domain.entity.DoorKind
import br.com.orcacor.domain.entity.MaterialConstants
import br.com.orcacor.domain.entity.MirrorKind
import br.com.orcacor.domain.entity.WindowKind

interface MaterialConstantsRepository {
    suspend fun getConstants(): MaterialConstants
    suspend fun getWindowKinds(): List<WindowKind>
    suspend fun getDoorKinds(): List<DoorKind>
    suspend fun getMirrorKinds(): List<MirrorKind>
    suspend fun getClosetKinds(): List<ClosetKind>
}

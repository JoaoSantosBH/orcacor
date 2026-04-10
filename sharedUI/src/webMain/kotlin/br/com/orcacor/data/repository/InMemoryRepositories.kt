package br.com.orcacor.data.repository

import br.com.orcacor.domain.entity.Budget
import br.com.orcacor.domain.entity.ClosetKind
import br.com.orcacor.domain.entity.DoorKind
import br.com.orcacor.domain.entity.MaterialConstants
import br.com.orcacor.domain.entity.MirrorKind
import br.com.orcacor.domain.entity.Room
import br.com.orcacor.domain.entity.User
import br.com.orcacor.domain.entity.WindowKind
import br.com.orcacor.domain.repository.BudgetRepository
import br.com.orcacor.domain.repository.MaterialConstantsRepository
import br.com.orcacor.domain.repository.RoomRepository
import br.com.orcacor.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

// ─── UserRepository ───────────────────────────────────────────────────────────

class UserRepositoryInMemory : UserRepository {
    private var current: User? = null

    override suspend fun getUser(): User? = current
    override suspend fun saveUser(user: User) { current = user }
    override suspend fun clearUser() { current = null }
}

// ─── BudgetRepository ─────────────────────────────────────────────────────────

class BudgetRepositoryInMemory : BudgetRepository {
    private val store = mutableMapOf<String, Budget>()
    private val flow = MutableStateFlow<List<Budget>>(emptyList())

    override fun getAll(): Flow<List<Budget>> = flow

    override suspend fun getById(id: String): Budget? = store[id]

    override suspend fun save(budget: Budget): Budget {
        store[budget.id] = budget
        flow.value = store.values.toList()
        return budget
    }

    override suspend fun delete(id: String) {
        store.remove(id)
        flow.value = store.values.toList()
    }
}

// ─── RoomRepository ───────────────────────────────────────────────────────────

class RoomRepositoryInMemory : RoomRepository {
    private val store = mutableMapOf<Long, Room>()
    private val flow = MutableStateFlow<List<Room>>(emptyList())
    private var nextId = 1L

    override fun getByBudgetId(budgetId: String): Flow<List<Room>> =
        flow.map { it.filter { r -> r.budgetId == budgetId } }

    override suspend fun save(room: Room): Room {
        val saved = if (room.id == null) room.copy(id = nextId++) else room
        store[saved.id!!] = saved
        flow.value = store.values.toList()
        return saved
    }

    override suspend fun delete(roomId: Long) {
        store.remove(roomId)
        flow.value = store.values.toList()
    }

    override suspend fun deleteByBudgetId(budgetId: String) {
        store.entries.removeAll { (_, r) -> r.budgetId == budgetId }
        flow.value = store.values.toList()
    }
}

// ─── MaterialConstantsRepository ─────────────────────────────────────────────

class MaterialConstantsRepositoryInMemory : MaterialConstantsRepository {

    override suspend fun getConstants(): MaterialConstants = MaterialConstants(
        sealer = 10f,
        plaster = 2f,
        paint = 10f,
        tow = 0.05f,
        maskingTape = 0.1f,
        sandpaper = 0.1f,
        wetSandpaper = 0.05f,
        plasticSheet = 1.2f,
        woolRoller = 0.02f,
        brush = 0.02f
    )

    override suspend fun getWindowKinds(): List<WindowKind> = listOf(
        WindowKind(1, "Janela Simples (0,60 x 1,20m)", 0.72f),
        WindowKind(2, "Janela Média (1,20 x 1,20m)", 1.44f),
        WindowKind(3, "Janela Grande (1,80 x 1,20m)", 2.16f),
        WindowKind(4, "Janela Box (0,60 x 0,60m)", 0.36f),
        WindowKind(5, "Janela Panorâmica (2,40 x 1,50m)", 3.60f)
    )

    override suspend fun getDoorKinds(): List<DoorKind> = listOf(
        DoorKind(1, "Porta Simples (0,80 x 2,10m)", 1.68f),
        DoorKind(2, "Porta Larga (0,90 x 2,10m)", 1.89f),
        DoorKind(3, "Porta Dupla (1,60 x 2,10m)", 3.36f),
        DoorKind(4, "Porta de Correr (2,00 x 2,10m)", 4.20f)
    )

    override suspend fun getMirrorKinds(): List<MirrorKind> = listOf(
        MirrorKind(1, "Espelho Pequeno (0,50 x 0,70m)", 0.35f),
        MirrorKind(2, "Espelho Médio (0,80 x 1,00m)", 0.80f),
        MirrorKind(3, "Espelho Grande (1,20 x 1,50m)", 1.80f),
        MirrorKind(4, "Espelho de Corpo Inteiro (0,60 x 1,80m)", 1.08f)
    )

    override suspend fun getClosetKinds(): List<ClosetKind> = listOf(
        ClosetKind(1, "Armário Embutido Pequeno (1,00 x 2,10m)", 2.10f),
        ClosetKind(2, "Armário Embutido Médio (1,50 x 2,10m)", 3.15f),
        ClosetKind(3, "Armário Embutido Grande (2,00 x 2,10m)", 4.20f),
        ClosetKind(4, "Armário de Cozinha (3,00 x 0,90m)", 2.70f)
    )
}

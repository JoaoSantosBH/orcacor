package br.com.orcacor.domain.usecase

import br.com.orcacor.domain.entity.MaterialConstants
import br.com.orcacor.domain.entity.Room
import br.com.orcacor.domain.entity.RoomKind
import br.com.orcacor.domain.usecase.material.CalculateMaterialsUseCase
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Testes do CalculateMaterialsUseCase baseados no gabarito do cliente.
 *
 * Os cômodos abaixo usam totalSquareMeters exatos do gabarito.
 * Isso isola completamente o teste de materiais do teste de cálculo de área.
 *
 * Gabarito de materiais (sistema legado, 02/05/2019):
 *   SELADORA  → 131.487 m² | 23.479822 L | rendimento 5.6 m²/L
 *               1 lata 18L + 1 galão 3,6L + 2 galões 900ml
 *   MASSA     → 131.487 m² | 52.5948 L  | rendimento 2.5 m²/L
 *               2 latas 18L + 4 galões 3,6L + 2 galões 900ml
 *   TINTA     → 580.751 m² | 29.7821 L  | rendimento 19.5 m²/L
 *               1 lata 18L + 3 galões 3,6L + 1 galão 900ml
 *   ÁREA TOTAL → 264.5855 m²
 */
class CalculateMaterialsUseCaseTest {

    private val useCase = CalculateMaterialsUseCase()

    /** Constantes de materiais do gabarito (rendimento em m²/L). */
    private val gabaritoConstants = MaterialConstants(
        sealer      = 5.6f,    // seladora: cobre 5.6 m² por litro
        plaster     = 2.5f,    // massa corrida: cobre 2.5 m² por litro
        paint       = 19.5f,   // tinta: cobre 19.5 m² por litro
        tow         = 0.05f,
        maskingTape = 0.1f,
        sandpaper   = 0.1f,
        wetSandpaper = 0.05f,
        plasticSheet = 1.2f,
        woolRoller  = 0.02f,
        brush       = 0.02f
    )

    /**
     * Cômodos com totalSquareMeters pré-calculado conforme gabarito.
     * Wallcoats e wallIsNew são os valores do gabarito.
     */
    private val gabaritoRooms = listOf(
        // 1. Quarto Casal — 41.82 m², 2 demãos, sem reboco
        Room(budgetId = "g", name = "Quarto Casal",
            kind = RoomKind.SYMMETRIC, totalSquareMeters = 41.82f, coats = 2, wallIsNew = false),
        // 2. Quarto Filhos — 37.019997 m², 2 demãos, com reboco
        Room(budgetId = "g", name = "Quarto Filhos",
            kind = RoomKind.SYMMETRIC, totalSquareMeters = 37.019997f, coats = 2, wallIsNew = true),
        // 3. Escritório — 66.44202 m², 2 demãos, sem reboco
        Room(budgetId = "g", name = "Escritório",
            kind = RoomKind.ASYMMETRIC, totalSquareMeters = 66.44202f, coats = 2, wallIsNew = false),
        // 4. Varanda Escritório — 13.536899 m², 2 demãos, com reboco
        Room(budgetId = "g", name = "Varanda Escritório",
            kind = RoomKind.EXTERNAL, totalSquareMeters = 13.536899f, coats = 2, wallIsNew = true),
        // 5. Varanda Quarto — 15.1866 m², 2 demãos, com reboco
        Room(budgetId = "g", name = "Varanda Quarto",
            kind = RoomKind.EXTERNAL, totalSquareMeters = 15.1866f, coats = 2, wallIsNew = true),
        // 6. Suite — 51.58 m², 3 demãos, sem reboco
        Room(budgetId = "g", name = "Suite",
            kind = RoomKind.SYMMETRIC, totalSquareMeters = 51.58f, coats = 3, wallIsNew = false),
        // 7. Banheiro Suite — 39.000004 m², 2 demãos, sem reboco
        Room(budgetId = "g", name = "Banheiro Suite",
            kind = RoomKind.SYMMETRIC, totalSquareMeters = 39.000004f, coats = 2, wallIsNew = false),
    )

    // ── Área total ─────────────────────────────────────────────────────────────

    @Test
    fun `soma das áreas do gabarito totaliza 264,5855 m²`() {
        val total = gabaritoRooms.sumOf { it.totalSquareMeters.toDouble() }.toFloat()
        assertApprox(264.5855f, total, delta = 0.01f, label = "Área total gabarito")
    }

    // ── Seladora ───────────────────────────────────────────────────────────────

    @Test
    fun `seladora - área aplicada apenas em cômodos com parede reboco`() {
        // Reboco: Quarto Filhos + Varanda Escritório + Varanda Quarto
        // Área seladora = Σ(area × coats): 37.019997×2 + 13.536899×2 + 15.1866×2 = 131.487 m²
        val materials = useCase.invoke(gabaritoRooms, gabaritoConstants)
        val seladora = materials.firstOrNull { it.name == "Seladora" }
        assertNotNull(seladora, "Seladora deve estar presente quando há cômodos com reboco")
        assertApprox(131.487f, seladora.totalArea, delta = 0.1f, label = "Seladora totalArea")
    }

    @Test
    fun `seladora - litros calculados com rendimento 5,6 m² por litro`() {
        val materials = useCase.invoke(gabaritoRooms, gabaritoConstants)
        val seladora = materials.first { it.name == "Seladora" }
        // 131.487 / 5.6 = 23.479822 L
        assertApprox(23.479822f, seladora.totalLiters, delta = 0.01f, label = "Seladora totalLiters")
    }

    @Test
    fun `seladora - embalagens corretas conforme gabarito`() {
        // 23.479822 L → 1×18L + 1×3,6L + 2×0,9L
        val materials = useCase.invoke(gabaritoRooms, gabaritoConstants)
        val seladora = materials.first { it.name == "Seladora" }
        assertEquals(1, seladora.cans18L, "Seladora: latas 18L")
        assertEquals(1, seladora.cans36L, "Seladora: galões 3,6L")
        assertEquals(2, seladora.cans09L, "Seladora: galões 900ml")
    }

    @Test
    fun `seladora NÃO é calculada quando não há cômodo com reboco`() {
        val roomsSemReboco = gabaritoRooms.map { it.copy(wallIsNew = false) }
        val materials = useCase.invoke(roomsSemReboco, gabaritoConstants)
        assertNull(
            actual = materials.firstOrNull { it.name == "Seladora" },
            message = "Seladora não deve aparecer quando wallIsNew=false em todos os cômodos"
        )
    }

    // ── Massa Corrida ──────────────────────────────────────────────────────────

    @Test
    fun `massa corrida - mesma área da seladora (131,487 m²)`() {
        val materials = useCase.invoke(gabaritoRooms, gabaritoConstants)
        val massa = materials.firstOrNull { it.name == "Massa Corrida" }
        assertNotNull(massa, "Massa Corrida deve estar presente quando há cômodos com reboco")
        assertApprox(131.487f, massa.totalArea, delta = 0.1f, label = "Massa Corrida totalArea")
    }

    @Test
    fun `massa corrida - litros calculados com rendimento 2,5 m² por litro`() {
        val materials = useCase.invoke(gabaritoRooms, gabaritoConstants)
        val massa = materials.first { it.name == "Massa Corrida" }
        // 131.487 / 2.5 = 52.5948 L
        assertApprox(52.5948f, massa.totalLiters, delta = 0.1f, label = "Massa Corrida totalLiters")
    }

    @Test
    fun `massa corrida - embalagens corretas conforme gabarito`() {
        // 52.5948 L → 2×18L + 4×3,6L + 2×0,9L
        val materials = useCase.invoke(gabaritoRooms, gabaritoConstants)
        val massa = materials.first { it.name == "Massa Corrida" }
        assertEquals(2, massa.cans18L, "Massa Corrida: latas 18L")
        assertEquals(4, massa.cans36L, "Massa Corrida: galões 3,6L")
        assertEquals(2, massa.cans09L, "Massa Corrida: galões 900ml")
    }

    @Test
    fun `massa corrida NÃO é calculada sem cômodos com reboco`() {
        val roomsSemReboco = gabaritoRooms.map { it.copy(wallIsNew = false) }
        val materials = useCase.invoke(roomsSemReboco, gabaritoConstants)
        assertNull(
            actual = materials.firstOrNull { it.name == "Massa Corrida" },
            message = "Massa Corrida não deve aparecer quando wallIsNew=false em todos os cômodos"
        )
    }

    // ── Tinta ──────────────────────────────────────────────────────────────────

    @Test
    fun `tinta - área considera TODOS os cômodos multiplicados pelos demãos`() {
        // Σ(area × coats): 41.82×2 + 37.02×2 + 66.44×2 + 13.54×2 + 15.19×2 + 51.58×3 + 39.0×2
        // = 83.64 + 74.04 + 132.88 + 27.07 + 30.37 + 154.74 + 78.0 = 580.751 m²
        val materials = useCase.invoke(gabaritoRooms, gabaritoConstants)
        val tinta = materials.first { it.name == "Tinta" }
        assertApprox(580.751f, tinta.totalArea, delta = 0.5f, label = "Tinta totalArea")
    }

    @Test
    fun `tinta - litros calculados com rendimento 19,5 m² por litro`() {
        val materials = useCase.invoke(gabaritoRooms, gabaritoConstants)
        val tinta = materials.first { it.name == "Tinta" }
        // 580.751 / 19.5 = 29.7821 L
        assertApprox(29.7821f, tinta.totalLiters, delta = 0.1f, label = "Tinta totalLiters")
    }

    @Test
    fun `tinta - embalagens corretas conforme gabarito`() {
        // 29.7821 L → 1×18L + 3×3,6L + 1×0,9L
        val materials = useCase.invoke(gabaritoRooms, gabaritoConstants)
        val tinta = materials.first { it.name == "Tinta" }
        assertEquals(1, tinta.cans18L, "Tinta: latas 18L")
        assertEquals(3, tinta.cans36L, "Tinta: galões 3,6L")
        assertEquals(1, tinta.cans09L, "Tinta: galões 900ml")
    }

    @Test
    fun `tinta inclui cômodos com reboco (não é exclusiva de cômodos sem reboco)`() {
        // Cômodos com wallIsNew=true também precisam ser pintados
        val materials = useCase.invoke(gabaritoRooms, gabaritoConstants)
        val tinta = materials.first { it.name == "Tinta" }
        // Área tinta não deve ser zero mesmo que todos tenham reboco
        assertTrue(tinta.totalArea > 0f, "Tinta deve ser calculada para todos os cômodos")
    }

    // ── Demãos ─────────────────────────────────────────────────────────────────

    @Test
    fun `demãos da Suite (3 demãos) multiplicam corretamente a área de tinta`() {
        val suiteOnly = listOf(
            Room(budgetId = "t", name = "Suite",
                kind = RoomKind.SYMMETRIC,
                totalSquareMeters = 51.58f, coats = 3, wallIsNew = false)
        )
        val materials = useCase.invoke(suiteOnly, gabaritoConstants)
        val tinta = materials.first { it.name == "Tinta" }
        assertApprox(51.58f * 3f, tinta.totalArea, delta = 0.1f, label = "Tinta Suite × 3 demãos")
    }

    @Test
    fun `demãos afetam tinta mas NÃO duplicam seladora além do esperado`() {
        // Seladora usa área × coats para cômodo com reboco
        val reboco = listOf(
            Room(budgetId = "t", name = "Reboco 1",
                kind = RoomKind.SYMMETRIC,
                totalSquareMeters = 10f, coats = 2, wallIsNew = true),
            Room(budgetId = "t", name = "Reboco 2",
                kind = RoomKind.SYMMETRIC,
                totalSquareMeters = 10f, coats = 3, wallIsNew = true)
        )
        val materials = useCase.invoke(reboco, gabaritoConstants)
        val seladora = materials.first { it.name == "Seladora" }
        // Área seladora = 10×2 + 10×3 = 50 m²
        assertApprox(50f, seladora.totalArea, delta = 0.1f, label = "Seladora área com demãos diferentes")
    }

    // ── Algoritmo de embalagens ────────────────────────────────────────────────

    @Test
    fun `algoritmo de embalagens - cálculo hierárquico 18L então 3,6L então 0,9L`() {
        // 23.479822 L:
        // 23.479822 / 18 = 1.3 → 1 lata 18L, sobra 5.479822
        //  5.479822 / 3.6 = 1.5 → 1 galão 3.6L, sobra 1.879822
        //  1.879822 / 0.9 = 2.08 → 2 galões 900ml, sobra 0.079822 (< 0.9, ok)
        val single = listOf(
            Room(budgetId = "t", name = "T",
                kind = RoomKind.SYMMETRIC,
                totalSquareMeters = 131.487f, coats = 1, wallIsNew = true)
        )
        val materials = useCase.invoke(single, gabaritoConstants)
        val sel = materials.first { it.name == "Seladora" }
        assertEquals(1, sel.cans18L, "Embalagem: latas 18L")
        assertEquals(1, sel.cans36L, "Embalagem: galões 3,6L")
        assertEquals(2, sel.cans09L, "Embalagem: galões 900ml")
    }

    @Test
    fun `todos os materiais de apoio são retornados mesmo sem reboco`() {
        val rooms = listOf(
            Room(budgetId = "t", name = "T",
                kind = RoomKind.SYMMETRIC,
                totalSquareMeters = 20f, coats = 1, wallIsNew = false)
        )
        val materials = useCase.invoke(rooms, gabaritoConstants)
        val names = materials.map { it.name }
        assertTrue("Tinta" in names, "Tinta deve estar nos materiais")
        assertTrue("Estopa" in names, "Estopa deve estar nos materiais")
        assertTrue("Fita Crepe" in names, "Fita Crepe deve estar nos materiais")
    }
}

// ── Helper de asserção com tolerância para Float ────────────────────────────
private fun assertApprox(expected: Float, actual: Float, delta: Float = 0.001f, label: String = "") {
    val diff = abs(expected - actual)
    assertTrue(
        actual = diff <= delta,
        message = "${if (label.isNotEmpty()) "[$label] " else ""}Expected $expected ± $delta, got $actual (diff=$diff)"
    )
}

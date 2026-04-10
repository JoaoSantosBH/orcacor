package br.com.orcacor.domain.usecase

import br.com.orcacor.domain.entity.Accessory
import br.com.orcacor.domain.entity.AccessoryType
import br.com.orcacor.domain.entity.Room
import br.com.orcacor.domain.entity.RoomKind
import br.com.orcacor.domain.usecase.room.CalculateRoomAreaUseCase
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Testes do CalculateRoomAreaUseCase baseados no gabarito do cliente.
 *
 * Gabarito original (resultado do sistema legado, data 02/05/2019):
 *   Quarto Casal          → 41.82 m²
 *   Quarto Filhos         → 37.019997 m²
 *   Escritório            → 66.44202 m²
 *   Varanda Escritório    → 13.536899 m²  (área externa)
 *   Varanda Quarto        → 15.1866 m²    (área externa, sem acessórios)
 *   Suite                 → 51.58 m²
 *   Banheiro Suite        → 39.000004 m²
 *   TOTAL                 → 264.5855 m²
 *
 * Áreas de acessórios utilizadas nestes testes:
 *   Porta  80×210 cm  = 0.80×2.10 = 1.68 m²
 *   Janela 100×150 cm = 1.00×1.50 = 1.50 m²
 *   Janela 120×150 cm = 1.20×1.50 = 1.80 m²
 *   Janela 120×120 cm = 1.20×1.20 = 1.44 m²
 *   Armário 2750×2750 mm = 2.75×2.75 = 7.5625 m²
 *
 * Nota: cômodos Suite e Banheiro Suite apresentam leve diferença em relação ao
 * gabarito legado porque o JSON antigo armazenava áreas de acessórios ligeiramente
 * diferentes (armário=7.56, janela120×120≈2.02). Para esses dois cômodos, os testes
 * verificam a lógica de cálculo com as dimensões padrão (2.75×2.75 e 1.20×1.20).
 */
class CalculateRoomAreaUseCaseTest {

    private val useCase = CalculateRoomAreaUseCase()

    // ── acessórios helper ──────────────────────────────────────────────────────
    private fun door80x210() = Accessory(AccessoryType.DOOR, 1, 1, 0.80f * 2.10f)
    private fun window100x150() = Accessory(AccessoryType.WINDOW, 1, 1, 1.00f * 1.50f)
    private fun window120x150() = Accessory(AccessoryType.WINDOW, 2, 1, 1.20f * 1.50f)
    private fun window120x120() = Accessory(AccessoryType.WINDOW, 3, 1, 1.20f * 1.20f)
    private fun closet2750x2750() = Accessory(AccessoryType.CLOSET, 1, 1, 2.75f * 2.75f)

    // ── Cômodo Simétrico ───────────────────────────────────────────────────────

    @Test
    fun `cômodo simétrico - Quarto Casal corresponde ao gabarito`() {
        // L=3 A=3 C=3 | 1 porta 80×210 | 1 janela 100×150 | 2 demãos
        // paredes = (3×2×3)+(3×2×3) = 36, teto = 3×3 = 9
        // descontos = 1.68 + 1.50 = 3.18 → total = 45 - 3.18 = 41.82 m²
        val room = Room(
            budgetId = "test", name = "Quarto Casal",
            kind = RoomKind.SYMMETRIC,
            width = 3f, height = 3f, length = 3f,
            doors = listOf(door80x210()),
            windows = listOf(window100x150()),
            coats = 2
        )
        val result = useCase.invoke(room)
        assertApprox(36f, result.wallsArea, delta = 0.001f, label = "wallsArea")
        assertApprox(9f, result.ceilingArea, delta = 0.001f, label = "ceilingArea")
        assertApprox(41.82f, result.totalSquareMeters, delta = 0.001f, label = "totalSquareMeters")
    }

    @Test
    fun `cômodo simétrico - Quarto Filhos (parede reboco) corresponde ao gabarito`() {
        // L=3 A=2.6 C=3 | 1 porta 80×210 | 1 janela 100×150 | reboco | 2 demãos
        // paredes = (3×2×2.6)+(3×2×2.6) = 31.2, teto = 9, gross = 40.2
        // descontos = 3.18 → total = 40.2 - 3.18 = 37.02 m²
        val room = Room(
            budgetId = "test", name = "Quarto Filhos",
            kind = RoomKind.SYMMETRIC,
            width = 3f, height = 2.6f, length = 3f,
            wallIsNew = true,
            doors = listOf(door80x210()),
            windows = listOf(window100x150()),
            coats = 2
        )
        val result = useCase.invoke(room)
        assertApprox(37.02f, result.totalSquareMeters, delta = 0.01f, label = "Quarto Filhos totalSquareMeters")
    }

    @Test
    fun `cômodo simétrico - Suite (armário, 3 demãos)`() {
        // L=4 A=3 C=4 | 2 portas | 1 janela | 1 armário 2.75×2.75 | 3 demãos
        // gross = 64, descontos = 3.36 + 1.50 + 7.5625 = 12.4225
        // total = 64 - 12.4225 = 51.5775 m²
        val room = Room(
            budgetId = "test", name = "Suite",
            kind = RoomKind.SYMMETRIC,
            width = 4f, height = 3f, length = 4f,
            doors = listOf(door80x210(), door80x210()),
            windows = listOf(window100x150()),
            closets = listOf(closet2750x2750()),
            coats = 3
        )
        val result = useCase.invoke(room)
        assertApprox(64f, result.wallsArea + result.ceilingArea, delta = 0.001f, label = "gross Suite")
        assertApprox(51.577f, result.totalSquareMeters, delta = 0.01f, label = "Suite totalSquareMeters")
    }

    @Test
    fun `cômodo simétrico - Banheiro Suite (janela 120×120)`() {
        // L=4 A=3 C=1.87 | 1 porta | 1 janela 120×120 | 2 demãos
        // paredes = (4×2×3)+(1.87×2×3) = 24+11.22 = 35.22
        // teto = 4×1.87 = 7.48, gross = 42.70
        // descontos = 1.68 + 1.44 = 3.12 → total = 39.58 m²
        val room = Room(
            budgetId = "test", name = "Banheiro Suite",
            kind = RoomKind.SYMMETRIC,
            width = 4f, height = 3f, length = 1.87f,
            doors = listOf(door80x210()),
            windows = listOf(window120x120()),
            coats = 2
        )
        val result = useCase.invoke(room)
        val expectedGross = (4f * 2f * 3f) + (1.87f * 2f * 3f) + (4f * 1.87f)
        assertApprox(expectedGross, result.wallsArea + result.ceilingArea, delta = 0.01f, label = "gross Banheiro")
        assertApprox(expectedGross - 1.68f - 1.44f, result.totalSquareMeters, delta = 0.01f, label = "Banheiro totalSquareMeters")
    }

    @Test
    fun `cômodo simétrico - wallIsNew não interfere no cálculo de área`() {
        val base = Room(
            budgetId = "t", name = "t", kind = RoomKind.SYMMETRIC,
            width = 3f, height = 3f, length = 3f
        )
        val withReboco = base.copy(wallIsNew = true)
        assertApprox(
            useCase.invoke(base).totalSquareMeters,
            useCase.invoke(withReboco).totalSquareMeters,
            delta = 0.001f,
            label = "wallIsNew não deve alterar a área"
        )
    }

    // ── Cômodo Assimétrico ─────────────────────────────────────────────────────

    @Test
    fun `cômodo assimétrico - Escritório (8 paredes, h=2,78) corresponde ao gabarito`() {
        // P1=3.8 P2=3.9 P3=5.4 P4=1.06 P5=1.03 P6=1.68 P7=0.21 P8=1.18
        // soma = 18.26, avg = 4.565, teto = 4.565² = 20.839
        // paredes = 18.26×2.78 = 50.763
        // descontos = 2×1.68 + 1.80 = 5.16
        // total = (50.763 - 5.16) + 20.839 = 66.44 m²  [gabarito: 66.44202]
        val walls = listOf(3.8f, 3.9f, 5.4f, 1.06f, 1.03f, 1.68f, 0.21f, 1.18f)
        val room = Room(
            budgetId = "test", name = "Escritório",
            kind = RoomKind.ASYMMETRIC,
            height = 2.78f,
            irregularWalls = walls,
            doors = listOf(door80x210(), door80x210()),
            windows = listOf(window120x150()),
            coats = 2
        )
        val result = useCase.invoke(room)
        val sum = walls.sum()
        val expectedCeiling = (sum / 4f) * (sum / 4f)
        assertApprox(expectedCeiling, result.ceilingArea, delta = 0.01f, label = "Escritório ceilingArea")
        assertApprox(66.44f, result.totalSquareMeters, delta = 0.01f, label = "Escritório totalSquareMeters")
    }

    @Test
    fun `cômodo assimétrico - desconto subtrai apenas das paredes (não do teto)`() {
        // Verifica ordem correta: total = (paredes - descontos) + teto
        // Bug da versão antiga: em alguns paths, ordem era diferente
        val walls = listOf(4f, 4f, 4f, 4f)  // square room 4m × 4m
        val height = 3f
        // soma = 16, avg = 4, teto = 16, paredes = 16×3 = 48
        val room = Room(
            budgetId = "test", name = "Quadrado Simétrico",
            kind = RoomKind.ASYMMETRIC,
            height = height,
            irregularWalls = walls,
            doors = listOf(door80x210())
        )
        val result = useCase.invoke(room)
        val expectedWalls = 16f * height         // 48
        val expectedCeiling = 4f * 4f            // 16
        val expectedTotal = (expectedWalls - 1.68f) + expectedCeiling  // (48-1.68)+16 = 62.32
        assertApprox(expectedTotal, result.totalSquareMeters, delta = 0.001f, label = "assimétrico com desconto")
    }

    // ── Área Externa ───────────────────────────────────────────────────────────

    @Test
    fun `área externa - Varanda Quarto (sem acessórios) corresponde ao gabarito`() {
        // L=4.29 A=3.54 | sem acessórios | reboco | 2 demãos
        // total = 4.29×3.54 = 15.1866 m²
        val room = Room(
            budgetId = "test", name = "Varanda Quarto",
            kind = RoomKind.EXTERNAL,
            width = 4.29f, height = 3.54f,
            wallIsNew = true,
            coats = 2
        )
        val result = useCase.invoke(room)
        assertApprox(0f, result.ceilingArea, delta = 0.001f, label = "Varanda Quarto ceilingArea deve ser 0")
        assertApprox(15.1866f, result.totalSquareMeters, delta = 0.005f, label = "Varanda Quarto totalSquareMeters")
    }

    @Test
    fun `área externa - Varanda Escritório (com porta e janela)`() {
        // L=4.3 A=3.93 | 1 porta 80×210 | 1 janela 100×150 | reboco | 2 demãos
        // total = 4.3×3.93 - 1.68 - 1.50 = 13.72 m²
        // (gabarito legado: 13.536899 — o JSON antigo usava área diferente para janela externa)
        val room = Room(
            budgetId = "test", name = "Varanda Escritório",
            kind = RoomKind.EXTERNAL,
            width = 4.3f, height = 3.93f,
            wallIsNew = true,
            doors = listOf(door80x210()),
            windows = listOf(window100x150()),
            coats = 2
        )
        val result = useCase.invoke(room)
        assertApprox(0f, result.ceilingArea, delta = 0.001f, label = "Varanda Escritório ceilingArea deve ser 0")
        val expectedTotal = 4.3f * 3.93f - 1.68f - 1.50f
        assertApprox(expectedTotal, result.totalSquareMeters, delta = 0.01f, label = "Varanda Escritório totalSquareMeters")
    }

    @Test
    fun `área externa - espelhos e armários NÃO são descontados`() {
        // Em áreas externas, somente janelas e portas são descontadas
        val room = Room(
            budgetId = "test", name = "Fachada",
            kind = RoomKind.EXTERNAL,
            width = 5f, height = 3f,
            mirrors = listOf(Accessory(AccessoryType.MIRROR, 1, 1, 2f)),
            closets = listOf(Accessory(AccessoryType.CLOSET, 1, 1, 3f))
        )
        val result = useCase.invoke(room)
        // mirrors e closets não devem ser descontados → total = 5×3 = 15
        assertApprox(15f, result.totalSquareMeters, delta = 0.001f, label = "EXTERNAL sem descontar espelhos/armários")
    }

    @Test
    fun `área nunca é negativa mesmo com muitos acessórios`() {
        val room = Room(
            budgetId = "test", name = "Sala Pequena",
            kind = RoomKind.SYMMETRIC,
            width = 1f, height = 2f, length = 1f,
            windows = listOf(
                Accessory(AccessoryType.WINDOW, 1, 1, 5f),  // area maior que a sala
                Accessory(AccessoryType.WINDOW, 2, 1, 5f)
            )
        )
        val result = useCase.invoke(room)
        assertTrue(result.totalSquareMeters >= 0f, "totalSquareMeters não pode ser negativa")
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

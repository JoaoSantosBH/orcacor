# CLAUDE.md — OrcaCor: Aplicativo de Orçamento de Pintura

> Guia completo de arquitetura, regras de negócio, stack e convenções do projeto OrcaCor.
> Leia este arquivo inteiro antes de iniciar qualquer implementação.

---

## 1. Visão Geral do Projeto

**Nome do App:** OrcaCor
**Package:** `br.com.orcacor`
**Plataformas:** Android (principal), Web (React), Desktop (Compose Multiplatform)
**Público-alvo:** Arquitetos e designers de interiores

### Propósito
OrcaCor é uma ferramenta profissional para levantamento de quantidade de tinta por metro quadrado de cada cômodo de um projeto. Gera relatórios de custos detalhados por cômodo e totais, que são enviados a profissionais de pintura para que estes elaborem orçamentos formais.

---

## 2. Repositório e Estrutura de Módulos

Todo o projeto vive em **único repositório monorepo** organizado por plataforma e camada:

```
orcacor/
├── androidApp/                  # Entry point Android
├── desktopApp/                  # Entry point Desktop (Compose)
├── webApp/                      # Frontend React
├── backend/                     # Spring Boot + Kotlin
├── shared/                      # Módulo KMP compartilhado
│   ├── domain/                  # Entidades, UseCases, interfaces de repositório
│   │   ├── entity/
│   │   ├── usecase/
│   │   └── repository/
│   ├── data/                    # Implementações de repositório, DAOs, mappers
│   │   ├── local/
│   │   │   ├── dao/
│   │   │   ├── entity/          # Room entities
│   │   │   └── mapper/
│   │   └── remote/
│   │       ├── api/
│   │       ├── dto/
│   │       └── mapper/
│   └── di/                      # Módulos Koin
├── composeUI/                   # Telas e componentes Compose Multiplatform
│   ├── theme/
│   ├── components/
│   └── screens/
│       ├── splash/
│       ├── onboarding/
│       ├── auth/
│       ├── home/
│       ├── budget/
│       ├── report/
│       ├── historic/
│       ├── profile/
│       └── instructions/
└── gradle/
    └── libs.versions.toml       # Version catalog
```

---

## 3. Tech Stack

### Mobile / Multiplatform
| Camada | Tecnologia | Versão |
|---|---|---|
| Linguagem | Kotlin Multiplatform | latest stable |
| UI | Compose Multiplatform | latest stable |
| Navegação | Compose Navigation 3 | latest stable |
| DI | Koin | latest stable |
| Banco local | Room (KMP) | latest stable |
| HTTP | Ktor Client (KMP) / Retrofit (Android) | latest stable |
| Serialização | kotlinx.serialization | latest stable |
| Imagens | Coil 3 (KMP) | latest stable |
| Async | Coroutines + Flow | latest stable |
| Plataforma Android | Android 16 (API 36) | minSdk 26 |

### Backend
| Camada | Tecnologia |
|---|---|
| Framework | Spring Boot 3.x + Kotlin |
| ORM | Spring Data JPA / Hibernate |
| Auth | Spring Security + JWT |
| Banco | PostgreSQL |
| Build | Gradle (Kotlin DSL) |

### Frontend Web
| Camada | Tecnologia |
|---|---|
| Framework | React 19 |
| Estado | Zustand ou React Query |
| UI | shadcn/ui + Tailwind CSS |
| HTTP | Axios |
| Linguagem | TypeScript |

---

## 4. Arquitetura

### Padrão Geral: Clean Architecture + MVI

```
Presentation (MVI)  →  Domain (UseCases)  →  Data (Repository Pattern)
```

### Regras por camada

**Domain** (`shared/domain/`)
- Contém apenas código Kotlin puro, **zero dependência** de framework
- `Entity`: modelos de domínio imutáveis (`data class`)
- `UseCase`: cada caso de uso em seu próprio arquivo, retorna `Flow<Result<T>>`
- `Repository`: interfaces que o Data implementa
- `Mapper`: interfaces de mapeamento entre camadas

**Data** (`shared/data/`)
- Implementa as interfaces de `Repository` do domain
- Separa `local` (Room) de `remote` (API)
- Cada DAO tem sua própria `entity` Room com mapper para domain
- DTOs de rede separados das entidades de domínio

**Presentation** (`composeUI/`)
- Segue padrão **MVI**: `Intent → State → Effect`
- Cada tela tem:
  - `ScreenViewModel` (herda de `BaseViewModel`)
  - `ScreenState` (data class imutável)
  - `ScreenIntent` (sealed interface)
  - `ScreenEffect` (sealed interface — eventos únicos como navegação)
- **Sem lógica de negócio na UI.** Toda regra passa por UseCase.

### Convenções de código
- Nomenclatura em **inglês** para código, **português** apenas para strings de UI
- `val` por padrão; `var` apenas onde mutabilidade é estritamente necessária
- Usar `sealed interface` para estados e efeitos
- Todos os UseCase retornam `Result<T>` ou `Flow<Result<T>>`
- Injeção de dependências **exclusivamente via Koin**
- Sem `!!` (non-null assertion) — usar elvis (`?:`) com fallback explícito

---

## 5. Identidade Visual e Design System

### Filosofia
Interface moderna, vanguardista e sofisticada. O público é composto de arquitetos e designers: a UI deve transmitir **precisão profissional**, **elegância minimalista** e **confiança técnica**.

### Material 3
- Usar **DynamicColorScheme** no Android (suporte a Material You)
- Fallback com paleta customizada para Desktop/Web
- Paleta sugerida: tons de `Indigo` ou `Deep Purple` como primary, `Teal` como secondary, fundo claro/escuro com elevação via `tonalElevation`

### Componentes obrigatórios
- `TopAppBar` com `LargeTopAppBar` nas telas principais
- `NavigationBar` (bottom) com 4 itens no menu principal
- `Card` com `shape = RoundedCornerShape(16.dp)` padrão
- `OutlinedTextField` com suporte a `prefix`, `suffix` e `supportingText`
- `Snackbar` para feedback de ação (nunca `Toast`)
- `ModalBottomSheet` para seleções de tipo (janela, porta, etc.)
- Animações com `AnimatedContent` e `AnimatedVisibility`
- `CircularProgressIndicator` / `LinearProgressIndicator` para estados de carregamento

### Typography
- Usar `MaterialTheme.typography` sem customização excessiva
- `displayLarge` / `headlineMedium` para títulos de telas
- `bodyLarge` para conteúdo principal
- `labelMedium` para labels de campos

### Espaçamento
- Grid base: `8.dp`
- Padding de tela: `16.dp` horizontal
- Espaçamento entre cards: `12.dp`

---

## 6. Telas e Navegação

### Mapa de Navegação (Compose Navigation 3)

```
SplashScreen
  └─► OnboardingScreen (4 páginas) — apenas primeira instalação
        └─► LoginScreen
              ├─► RegisterScreen
              └─► HomeScreen (BottomNav)
                    ├─► BudgetScreen (novo orçamento)
                    │     ├─► RoomFormScreen (adicionar/editar cômodo)
                    │     └─► ReportScreen (visualizar relatório)
                    ├─► HistoricScreen (orçamentos salvos)
                    │     └─► ReportScreen (visualizar orçamento existente)
                    ├─► ProfileScreen
                    └─► InstructionsScreen
```

### Detalhe de cada tela

#### SplashScreen
- Logo animado com `AnimatedVisibility` + escala
- Verificar se é primeiro acesso (SharedPreferences/DataStore) → redirecionar Onboarding ou Login
- Verificar sessão ativa → redirecionar Home se logado

#### OnboardingScreen
- 4 páginas com `HorizontalPager`
- Conteúdo de cada página:
  1. "Levante a área de pintura com precisão" — ícone de régua/planta
  2. "Calcule tinta por cômodo automaticamente" — ícone de tinta
  3. "Gere relatórios profissionais" — ícone de documento
  4. "Envie para pintores e receba orçamentos" — ícone de compartilhamento
- Indicadores de página (`PageIndicator`)
- Botão "Pular" e "Próximo" / "Começar"

#### LoginScreen
- Campos: Email, Senha
- Botão "Entrar"
- Link "Criar conta"
- Link "Esqueci minha senha" (futuro)
- Validação local antes de chamar API

#### RegisterScreen
- Campos: Nome completo, E-mail, Celular (com máscara), Senha, Confirmar senha
- Validação de formato de e-mail e celular
- Botão "Cadastrar"
- Link "Já tenho conta"

#### HomeScreen (BottomNav)
- 4 itens: Orçamento, Histórico, Perfil, Instruções
- `NavigationBar` fixo na base
- `AnimatedContent` na troca de destinos

#### BudgetScreen
- Lista de cômodos já adicionados ao orçamento atual (se houver)
- FAB "+" para adicionar novo cômodo
- Campo para nome do destinatário do orçamento (profissional de pintura)
- Campo para e-mail do destinatário
- Botão "Gerar Relatório" (habilitado quando há pelo menos 1 cômodo)
- Card resumo: total de m², total de tintas estimadas

#### RoomFormScreen
- Ver seção 7 (Tela de Orçamento de Cômodo) — detalhe completo abaixo

#### ReportScreen
- Cabeçalho com número do orçamento, data, dados do profissional
- Lista de cômodos com área total de cada um
- Seção de materiais estimados (seladora, massa, tinta)
- Totais gerais
- Botão "Compartilhar PDF" — gera PDF e abre share sheet
- Botão "Enviar por e-mail" / WhatsApp

#### HistoricScreen
- Lista de orçamentos salvos (`LazyColumn`)
- Cada item: número do orçamento, data, destinatário, total m²
- Swipe para deletar (com confirmação via `AlertDialog`)
- Toque abre ReportScreen do orçamento selecionado

#### ProfileScreen
- Campos editáveis: Nome, E-mail, Celular
- Botão "Salvar alterações"
- Botão "Sair" (logout)

#### InstructionsScreen
- Conteúdo textual com passo a passo do uso do app
- Link para vídeo tutorial (YouTube)
- Scroll livre com `LazyColumn`

---

## 7. Tela de Orçamento de Cômodo (RoomFormScreen)

Esta é a tela central do app. Deve ser fluida, com seções expansíveis e feedback visual rico.

### Estrutura da tela

```
┌─ Nome do Cômodo (obrigatório) ─────────────────────┐
│  OutlinedTextField                                   │
├─ Observação ────────────────────────────────────────┤
│  OutlinedTextField (multiline)                       │
├─ Dimensões do Cômodo ───────────────────────────────┤
│  Largura (m) | Comprimento (m) | Altura (m)         │
│  [Campo numérico com sufixo "m"]                    │
├─ Condição da Parede ────────────────────────────────┤
│  Switch: "Parede em reboco (nova)"                  │
├─ Cor Desejada ──────────────────────────────────────┤
│  OutlinedTextField + ícone de paleta de cores       │
├─ Foto do Cômodo ────────────────────────────────────┤
│  [Adicionar foto] (até 1 foto)                      │
├─ Acessórios ────────────────────────────────────────┤
│  ┌── Janelas ─────────────────────────────────────┐ │
│  │  Quantidade + Tipo (ModalBottomSheet)           │ │
│  │  [+ Foto] (até 2 fotos por item)               │ │
│  └─────────────────────────────────────────────── ┘ │
│  ┌── Portas ──────────────────────────────────────┐ │
│  │  Quantidade + Tipo                              │ │
│  │  [+ Foto] (até 2 fotos por item)               │ │
│  └─────────────────────────────────────────────── ┘ │
│  ┌── Espelhos ─────────────────────────────────────┐ │
│  │  Quantidade + Tipo                              │ │
│  │  [+ Foto]                                       │ │
│  └─────────────────────────────────────────────── ┘ │
│  ┌── Armários ─────────────────────────────────────┐ │
│  │  Quantidade + Tipo                              │ │
│  │  [+ Foto]                                       │ │
│  └─────────────────────────────────────────────── ┘ │
├─ Resumo calculado (live preview) ──────────────────┤
│  Área bruta: X m² | Descontos: Y m² | Total: Z m²  │
└─ [Salvar Cômodo] ───────────────────────────────────┘
```

### Tipos de Cômodo
Os tipos são carregados de JSON local (ou da API):
- **Simétrico** (cômodo retangular): largura × comprimento × altura
- **Assimétrico** (perímetro irregular): soma de paredes individuais × altura
- **Externo/Fachada**: apenas largura × altura (sem teto)

### Campos de Foto
- Cada cômodo: até **1 foto**
- Cada item de acessório (janela, porta, espelho, armário): até **2 fotos**
- Fotos armazenadas localmente (Room + file system)
- Exibidas em `Row` com `AsyncImage` (Coil)
- Picker via `ActivityResultContracts.GetContent` (galeria) ou câmera

---

## 8. Regras de Negócio e Cálculos

### 8.1 Cálculo de Área por Tipo de Cômodo

#### Cômodo Simétrico (retangular)
```kotlin
val paredesArea = ((largura * 2) * altura) + ((comprimento * 2) * altura)
val tetoArea = largura * comprimento
val areaBase = paredesArea + tetoArea
val areaTotal = areaBase - janelas - portas - espelhos - armários
```

#### Cômodo Assimétrico (perímetro irregular)
```kotlin
// doorList = lista de comprimentos de cada parede
val somatoria = doorList.sum()
val paredesArea = somatoria * altura
val media = somatoria / 4
val tetoArea = media * media
val areaTotal = (paredesArea - janelas - portas - espelhos - armários) + tetoArea
```

#### Cômodo Externo / Fachada
```kotlin
val areaBase = largura * altura
val areaTotal = areaBase - janelas - portas  // sem teto, sem espelhos/armários
room.teto = 0f
```

### 8.2 Desconto de Acessórios

Cada tipo de acessório possui uma área padrão cadastrada:

| Tipo | Entidade | Campo de área |
|---|---|---|
| Janela | `WindowKind` | `area: Float` (m²) |
| Porta | `DoorKind` | `area: Float` (m²) |
| Espelho | `MirrorKind` | `area: Float` (m²) |
| Armário | `ClosetKind` | `area: Float` (m²) |

Desconto = `quantidade × areaDoTipo`

### 8.3 Cálculo de Materiais

#### Materiais calculados por m² total do orçamento:

| Material | Constante | Unidade |
|---|---|---|
| Seladora | `seladora_litrom2` | litros/m² |
| Massa corrida | `massa_litrom2` | litros/m² |
| Tinta | `tinta_litrom2` | litros/m² |
| Estopa | `estopam2` | qtd/m² |
| Fita crepe | `fitacrepem2` | qtd/m² |
| Lixa seca | `lixa_qtym2` | qtd/m² |
| Lixa d'água | `lixaagua_qtym2` | qtd/m² |
| Lona plástica | `lona_plasticam2` | m²/m² |
| Rolo de lã | `rolo_lam2` | qtd/m² |
| Trincha | `trincham2` | qtd/m² |

**Se parede está em reboco (nova):** aplicar seladora e massa corrida
**Se parede não está em reboco:** pular seladora e massa corrida

#### Conversão de litros em embalagens:
```kotlin
// Lata 18L (latão)
fun calcularLata18L(litros: Float): Int = (litros / 18).toInt()

// Lata 3,6L (galão)
fun calcularLata36L(litros: Float): Int = (litros / 3.6).toInt()

// Lata 0,9L (galãozinho)
fun calcularLata09L(litros: Float): Int = (litros / 0.9).toInt()
```

Algoritmo de cálculo de embalagens:
1. Calcular litros totais = `area / rendimentoPorLitro`
2. Dividir pela maior embalagem (18L) → registrar latas inteiras
3. Resto: dividir por 3,6L → registrar galões inteiros
4. Resto: dividir por 0,9L → registrar galãozinhos
5. Guardar `diferença` (sobra ou falta para completar próxima embalagem)

### 8.4 Número de Demãos
- Campo `demaos: Int` no cômodo
- Multiplicar área total pelo número de demãos antes de calcular tinta

### 8.5 Parede Nova (Reboco)
- `wallIsNew: Boolean`
- Se `true`: calcular seladora e massa corrida além da tinta
- Se `false`: calcular apenas tinta (e materiais de acabamento)

---

## 9. Entidades de Domínio

Todas as entidades ficam em `shared/domain/entity/`. São `data class` puras, sem anotações de framework.

### Room (Cômodo)
```kotlin
data class Room(
    val id: Long? = null,
    val budgetId: String,           // FK para Budget
    val name: String,               // nome do cômodo
    val kind: RoomKind,             // SYMMETRIC, ASYMMETRIC, EXTERNAL
    val note: String? = null,
    val wallIsNew: Boolean = false,
    val desiredColor: String? = null,
    val width: Float? = null,
    val height: Float? = null,
    val length: Float? = null,
    val irregularWalls: List<Float> = emptyList(), // para assimétrico
    val doors: List<Accessory> = emptyList(),
    val windows: List<Accessory> = emptyList(),
    val mirrors: List<Accessory> = emptyList(),
    val closets: List<Accessory> = emptyList(),
    val photos: List<String> = emptyList(),       // paths locais
    val ceilingArea: Float = 0f,                  // calculado
    val wallsArea: Float = 0f,                    // calculado
    val totalSquareMeters: Float = 0f,            // calculado (líquido)
    val coats: Int = 2                            // número de demãos
)

enum class RoomKind { SYMMETRIC, ASYMMETRIC, EXTERNAL }
```

### Accessory (Acessório genérico)
```kotlin
data class Accessory(
    val type: AccessoryType,       // DOOR, WINDOW, MIRROR, CLOSET
    val kindId: Int,               // ID do tipo (DoorKind, WindowKind, etc.)
    val quantity: Int,
    val area: Float,               // área do tipo × quantidade
    val photos: List<String> = emptyList()
)

enum class AccessoryType { DOOR, WINDOW, MIRROR, CLOSET }
```

### Budget (Orçamento)
```kotlin
data class Budget(
    val id: String,                // UUID
    val number: String,            // número formatado ex: "ORC-2024-001"
    val date: LocalDate,
    val recipientName: String,     // nome do profissional de pintura
    val recipientEmail: String,
    val rooms: List<Room> = emptyList(),
    val totalArea: Float = 0f,     // calculado — soma de todos os cômodos
    val status: BudgetStatus = BudgetStatus.DRAFT
)

enum class BudgetStatus { DRAFT, SENT, ARCHIVED }
```

### User (Usuário — arquiteto/designer)
```kotlin
data class User(
    val id: String,
    val name: String,
    val email: String,
    val phone: String
)
```

### AccessoryKind (Tipos cadastrados)
```kotlin
data class WindowKind(val id: Int, val name: String, val area: Float)
data class DoorKind(val id: Int, val name: String, val area: Float)
data class MirrorKind(val id: Int, val name: String, val area: Float)
data class ClosetKind(val id: Int, val name: String, val area: Float)
```

### MaterialConstants (Constantes de cálculo)
```kotlin
data class MaterialConstants(
    val sealer: Float,           // seladora_litrom2
    val plaster: Float,          // massa_litrom2
    val paint: Float,            // tinta_litrom2
    val tow: Float,              // estopam2
    val maskingtape: Float,      // fitacrepem2
    val sandpaper: Float,        // lixa_qtym2
    val wetSandpaper: Float,     // lixaagua_qtym2
    val plasticSheet: Float,     // lona_plasticam2
    val woolRoller: Float,       // rolo_lam2
    val brush: Float             // trincham2
)
```

### Report (Relatório gerado)
```kotlin
data class Report(
    val budget: Budget,
    val user: User,
    val rooms: List<Room>,
    val materials: List<MaterialEstimate>,
    val accessories: List<String>,  // observações/itens extras
    val totalArea: Float,
    val generatedAt: LocalDateTime
)

data class MaterialEstimate(
    val name: String,
    val totalArea: Float,
    val yieldPerLiter: Float,
    val totalLiters: Float,
    val cans18L: Int,
    val cans36L: Int,
    val cans09L: Int,
    val remainder: Float
)
```

---

## 10. UseCases

Localização: `shared/domain/usecase/`

```
budget/
  CreateBudgetUseCase.kt
  SaveBudgetUseCase.kt
  GetBudgetHistoryUseCase.kt
  DeleteBudgetUseCase.kt
  SendBudgetReportUseCase.kt

room/
  AddRoomToBudgetUseCase.kt
  CalculateRoomAreaUseCase.kt
  UpdateRoomUseCase.kt
  DeleteRoomUseCase.kt

report/
  GenerateReportUseCase.kt
  ExportReportToPdfUseCase.kt
  ShareReportUseCase.kt

material/
  CalculateMaterialsUseCase.kt    ← lógica central de cálculo de tintas

auth/
  LoginUseCase.kt
  RegisterUseCase.kt
  LogoutUseCase.kt
  GetCurrentUserUseCase.kt

accessories/
  GetWindowTypesUseCase.kt
  GetDoorTypesUseCase.kt
  GetMirrorTypesUseCase.kt
  GetClosetTypesUseCase.kt
```

---

## 11. Módulos Koin (DI)

```kotlin
// shared/di/

val domainModule = module {
    factory { CreateBudgetUseCase(get()) }
    factory { CalculateRoomAreaUseCase() }
    factory { CalculateMaterialsUseCase(get()) }
    // ... demais UseCases
}

val dataModule = module {
    single { createRoomDatabase(get()) }
    single { get<AppDatabase>().budgetDao() }
    single { get<AppDatabase>().roomDao() }
    single<BudgetRepository> { BudgetRepositoryImpl(get(), get()) }
    single<RoomRepository> { RoomRepositoryImpl(get()) }
    single<MaterialConstantsRepository> { MaterialConstantsRepositoryImpl(get()) }
    single { createHttpClient() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
}

val presentationModule = module {
    viewModel { BudgetViewModel(get(), get(), get()) }
    viewModel { RoomFormViewModel(get(), get(), get(), get()) }
    viewModel { ReportViewModel(get(), get()) }
    viewModel { HistoricViewModel(get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { AuthViewModel(get(), get()) }
}
```

---

## 12. Banco de Dados Local (Room KMP)

### Entidades Room (Data Layer)
```kotlin
@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey val id: String,
    val number: String,
    val date: Long,          // epoch millis
    val recipientName: String,
    val recipientEmail: String,
    val status: String
)

@Entity(tableName = "rooms", foreignKeys = [
    ForeignKey(entity = BudgetEntity::class, parentColumns = ["id"], childColumns = ["budgetId"], onDelete = CASCADE)
])
data class RoomEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val budgetId: String,
    val name: String,
    val kind: String,
    val note: String?,
    val wallIsNew: Boolean,
    val desiredColor: String?,
    val width: Float?,
    val height: Float?,
    val length: Float?,
    val ceilingArea: Float,
    val wallsArea: Float,
    val totalSquareMeters: Float,
    val coats: Int
)

@Entity(tableName = "accessories")
data class AccessoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val roomId: Long,
    val type: String,
    val kindId: Int,
    val quantity: Int,
    val area: Float
)

@Entity(tableName = "room_photos")
data class RoomPhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val roomId: Long,
    val accessoryId: Long?,   // null = foto do cômodo
    val filePath: String
)
```

### DAOs
```
BudgetDao     → insert, update, delete, getAll, getById, getWithRooms
RoomDao       → insert, update, delete, getByBudgetId
AccessoryDao  → insert, deleteByRoom, getByRoom
RoomPhotoDao  → insert, deleteByRoom, getByRoom
UserDao       → insert, update, get (single user local)
```

---

## 13. API Backend (Spring Boot)

### Endpoints principais

```
Auth
  POST   /api/auth/login
  POST   /api/auth/register
  POST   /api/auth/refresh

Budgets
  GET    /api/budgets                  → lista do usuário autenticado
  POST   /api/budgets                  → criar orçamento
  GET    /api/budgets/{id}             → detalhe
  PUT    /api/budgets/{id}             → atualizar
  DELETE /api/budgets/{id}             → deletar
  POST   /api/budgets/{id}/send        → enviar relatório por e-mail

References (dados de referência)
  GET    /api/references/window-types
  GET    /api/references/door-types
  GET    /api/references/mirror-types
  GET    /api/references/closet-types
  GET    /api/references/material-constants
```

---

## 14. Geração e Compartilhamento de PDF

- Usar biblioteca **iText** (Android/JVM) ou **PDFBox** para geração
- No KMP: abstrair atrás de `interface PdfGenerator` com implementação por plataforma
- Estrutura do PDF:
  1. Cabeçalho: logo OrcaCor, número do orçamento, data, dados do profissional destinatário
  2. Dados do elaborador (arquiteto/designer)
  3. Tabela de cômodos: nome, dimensões, área bruta, descontos, área líquida
  4. Tabela de materiais: seladora, massa, tinta — com detalhamento de embalagens
  5. Tabela de materiais de apoio: estopa, lixa, fita, rolo, etc.
  6. Rodapé: "Elaborado com OrcaCor"

- Compartilhamento via `FileProvider` (Android) / share API nativa

---

## 15. Observações de Migração dos old_files

Os arquivos em `old_files/` são de uma versão anterior do app (Android View/MVVM). Ao migrar:

- **Manter:** toda a lógica de cálculo de `SimetricRoomViewModel`, `AssimetricRoomViewModel` e `ExternalRoomViewModel` — extrair para `CalculateRoomAreaUseCase`
- **Manter:** lógica de `CalculateMaterials.calculateAmountMaterialLiters()` — extrair para `CalculateMaterialsUseCase`
- **Manter:** estrutura de dados de `Constantes` (constantes de cálculo por m²)
- **Adaptar:** entidades `Room`, `Budget`, `User` para o novo modelo sem dependências Android
- **Descartar:** toda a camada de View (Activities, Fragments, Adapters, ViewHolders)
- **Descartar:** `BaseFragment`, `BaseAdapter`, `BaseViewHolder`, `LiveData` extensions — substituir por Compose + StateFlow
- **Descartar:** `Parcelable` — substituir por serialização via `kotlinx.serialization`
- **Atenção:** o campo `demaos` (número de demãos) estava na entidade antiga — **deve ser mantido** na nova entidade Room
- **Atenção:** o bug no `AssimetricRoomViewModel` onde `paredes` era subtraído de `janelas/portas` antes de somar o teto deve ser **corrigido** na nova implementação (ordem correta: `areaTotal = (paredes - descontos) + teto`)

---

## 16. Checklist de Início de Projeto

- [ ] Configurar `libs.versions.toml` com todas as dependências
- [ ] Configurar módulos Gradle: `shared`, `androidApp`, `desktopApp`, `webApp`, `backend`
- [ ] Configurar Koin com módulos `domainModule`, `dataModule`, `presentationModule`
- [ ] Implementar Room Database com todas as entidades e DAOs
- [ ] Implementar `CalculateRoomAreaUseCase` (portar lógica dos ViewModels antigos)
- [ ] Implementar `CalculateMaterialsUseCase` (portar lógica de `CalculateMaterials`)
- [ ] Configurar tema Material 3 com paleta personalizada
- [ ] Implementar `SplashScreen` com verificação de sessão
- [ ] Implementar `OnboardingScreen` com controle de primeira execução
- [ ] Implementar fluxo de autenticação (Login + Registro)
- [ ] Implementar `RoomFormScreen` com cálculo ao vivo (live preview)
- [ ] Implementar `ReportScreen` com geração de PDF
- [ ] Carregar tipos de acessórios do JSON local (fallback) e da API
- [ ] Configurar `FileProvider` para fotos e PDF
- [ ] Implementar compartilhamento de relatório (e-mail + WhatsApp)
- [ ] Testes unitários para todos os UseCases de cálculo

---

## 17. Convenções de Commit e Branch

```
feat(scope): descrição em português
fix(scope): descrição
refactor(scope): descrição
test(scope): descrição

Scopes: auth, budget, room, report, historic, profile, ui, di, db, api
```

Branch principal: `main`
Feature branches: `feat/nome-da-feature`
Hotfixes: `fix/nome-do-fix`

---

*Documento gerado em 2026-03-27. Mantenha este arquivo atualizado conforme o projeto evolui.*

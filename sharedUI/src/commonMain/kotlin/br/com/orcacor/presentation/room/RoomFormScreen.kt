package br.com.orcacor.presentation.room

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.orcacor.domain.entity.AccessoryType
import br.com.orcacor.domain.entity.RoomKind
import br.com.orcacor.util.formatFloat
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomFormScreen(
    budgetId: String,
    onNavigateBack: () -> Unit,
    viewModel: RoomFormViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(budgetId) {
        viewModel.onIntent(RoomFormIntent.SetBudgetId(budgetId))
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                RoomFormEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onIntent(RoomFormIntent.ClearError)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Cômodo") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Room name
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { viewModel.onIntent(RoomFormIntent.UpdateName(it)) },
                    label = { Text("Nome do cômodo *") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true
                )
            }

            item {
                // Room kind selector
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = state.kind.label(),
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Tipo de cômodo") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        RoomKind.entries.forEach { kind ->
                            DropdownMenuItem(
                                text = { Text(kind.label()) },
                                onClick = {
                                    viewModel.onIntent(RoomFormIntent.UpdateKind(kind))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                // Dimensions
                Text("Dimensões", style = MaterialTheme.typography.titleSmall)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (state.kind != RoomKind.ASYMMETRIC) {
                        OutlinedTextField(
                            value = state.width,
                            onValueChange = { viewModel.onIntent(RoomFormIntent.UpdateWidth(it)) },
                            label = { Text("Largura") },
                            suffix = { Text("m") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                            singleLine = true
                        )
                    }
                    OutlinedTextField(
                        value = state.height,
                        onValueChange = { viewModel.onIntent(RoomFormIntent.UpdateHeight(it)) },
                        label = { Text("Altura") },
                        suffix = { Text("m") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                        singleLine = true
                    )
                    if (state.kind == RoomKind.SYMMETRIC) {
                        OutlinedTextField(
                            value = state.length,
                            onValueChange = { viewModel.onIntent(RoomFormIntent.UpdateLength(it)) },
                            label = { Text("Comprimento") },
                            suffix = { Text("m") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
                            singleLine = true
                        )
                    }
                }
            }

            item {
                // Asymmetric walls input
                if (state.kind == RoomKind.ASYMMETRIC) {
                    AsymmetricWallsInput(
                        walls = state.irregularWalls,
                        onChange = { viewModel.onIntent(RoomFormIntent.UpdateIrregularWalls(it)) }
                    )
                }
            }

            item {
                // Wall condition
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Parede em reboco (nova)", style = MaterialTheme.typography.bodyLarge)
                        Text("Calcular seladora e massa corrida", style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Switch(
                        checked = state.wallIsNew,
                        onCheckedChange = { viewModel.onIntent(RoomFormIntent.UpdateWallIsNew(it)) }
                    )
                }
            }

            item {
                // Color
                OutlinedTextField(
                    value = state.desiredColor,
                    onValueChange = { viewModel.onIntent(RoomFormIntent.UpdateDesiredColor(it)) },
                    label = { Text("Cor desejada") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true
                )
            }

            item {
                // Coats
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                    OutlinedTextField(
                        value = "${state.coats} demão${if (state.coats > 1) "s" else ""}",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Número de demãos") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        (1..4).forEach { coats ->
                            DropdownMenuItem(
                                text = { Text("$coats demão${if (coats > 1) "s" else ""}") },
                                onClick = {
                                    viewModel.onIntent(RoomFormIntent.UpdateCoats(coats))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                HorizontalDivider()
                Text("Acessórios", style = MaterialTheme.typography.titleMedium)
            }

            // Accessories sections
            if (state.doorKinds.isNotEmpty()) {
                item {
                    AccessorySection(
                        title = "Portas",
                        kinds = state.doorKinds.map { it.id to it.name },
                        kindAreas = state.doorKinds.associate { it.id to it.area },
                        added = state.doors.map { "${it.quantity}x ${it.kindId}" },
                        type = AccessoryType.DOOR,
                        onAdd = { kindId, qty, area ->
                            viewModel.onIntent(RoomFormIntent.AddAccessory(AccessoryType.DOOR, kindId, qty, area))
                        },
                        onRemove = { idx ->
                            viewModel.onIntent(RoomFormIntent.RemoveAccessory(AccessoryType.DOOR, idx))
                        }
                    )
                }
            }

            if (state.windowKinds.isNotEmpty()) {
                item {
                    AccessorySection(
                        title = "Janelas",
                        kinds = state.windowKinds.map { it.id to it.name },
                        kindAreas = state.windowKinds.associate { it.id to it.area },
                        added = state.windows.map { "${it.quantity}x ${it.kindId}" },
                        type = AccessoryType.WINDOW,
                        onAdd = { kindId, qty, area ->
                            viewModel.onIntent(RoomFormIntent.AddAccessory(AccessoryType.WINDOW, kindId, qty, area))
                        },
                        onRemove = { idx ->
                            viewModel.onIntent(RoomFormIntent.RemoveAccessory(AccessoryType.WINDOW, idx))
                        }
                    )
                }
            }

            if (state.mirrorKinds.isNotEmpty() && state.kind != RoomKind.EXTERNAL) {
                item {
                    AccessorySection(
                        title = "Espelhos",
                        kinds = state.mirrorKinds.map { it.id to it.name },
                        kindAreas = state.mirrorKinds.associate { it.id to it.area },
                        added = state.mirrors.map { "${it.quantity}x ${it.kindId}" },
                        type = AccessoryType.MIRROR,
                        onAdd = { kindId, qty, area ->
                            viewModel.onIntent(RoomFormIntent.AddAccessory(AccessoryType.MIRROR, kindId, qty, area))
                        },
                        onRemove = { idx ->
                            viewModel.onIntent(RoomFormIntent.RemoveAccessory(AccessoryType.MIRROR, idx))
                        }
                    )
                }
            }

            if (state.closetKinds.isNotEmpty() && state.kind != RoomKind.EXTERNAL) {
                item {
                    AccessorySection(
                        title = "Armários",
                        kinds = state.closetKinds.map { it.id to it.name },
                        kindAreas = state.closetKinds.associate { it.id to it.area },
                        added = state.closets.map { "${it.quantity}x ${it.kindId}" },
                        type = AccessoryType.CLOSET,
                        onAdd = { kindId, qty, area ->
                            viewModel.onIntent(RoomFormIntent.AddAccessory(AccessoryType.CLOSET, kindId, qty, area))
                        },
                        onRemove = { idx ->
                            viewModel.onIntent(RoomFormIntent.RemoveAccessory(AccessoryType.CLOSET, idx))
                        }
                    )
                }
            }

            item {
                // Live preview
                HorizontalDivider()
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Resumo calculado", style = MaterialTheme.typography.titleSmall)
                        Spacer(Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Paredes", style = MaterialTheme.typography.labelSmall)
                                Text("${formatFloat(state.previewWallsArea)} m²")
                            }
                            Column {
                                Text("Teto", style = MaterialTheme.typography.labelSmall)
                                Text("${formatFloat(state.previewCeilingArea)} m²")
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Total líquido", style = MaterialTheme.typography.labelSmall)
                                Text(
                                    "${formatFloat(state.previewTotalArea)} m²",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Observation
                OutlinedTextField(
                    value = state.note,
                    onValueChange = { viewModel.onIntent(RoomFormIntent.UpdateNote(it)) },
                    label = { Text("Observação") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4
                )
            }

            item {
                Button(
                    onClick = { viewModel.onIntent(RoomFormIntent.SaveRoom) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) CircularProgressIndicator(modifier = Modifier.height(20.dp))
                    else Text("Salvar Cômodo")
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AsymmetricWallsInput(walls: List<Float>, onChange: (List<Float>) -> Unit) {
    Column {
        Text("Comprimento das paredes", style = MaterialTheme.typography.titleSmall)
        walls.forEachIndexed { index, value ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = if (value == 0f) "" else value.toString(),
                    onValueChange = { input ->
                        val newWalls = walls.toMutableList()
                        newWalls[index] = input.toFloatOrNull() ?: 0f
                        onChange(newWalls)
                    },
                    label = { Text("Parede ${index + 1}") },
                    suffix = { Text("m") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                    singleLine = true
                )
                IconButton(onClick = { onChange(walls.toMutableList().also { it.removeAt(index) }) }) {
                    Text("✕")
                }
            }
            Spacer(Modifier.height(4.dp))
        }
        Button(
            onClick = { onChange(walls + 0f) },
            modifier = Modifier.fillMaxWidth()
        ) { Text("+ Adicionar parede") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AccessorySection(
    title: String,
    kinds: List<Pair<Int, String>>,
    kindAreas: Map<Int, Float>,
    added: List<String>,
    type: AccessoryType,
    onAdd: (kindId: Int, quantity: Int, area: Float) -> Unit,
    onRemove: (index: Int) -> Unit
) {
    var selectedKindId by rememberSaveable { mutableStateOf(kinds.first().first) }
    var quantity by rememberSaveable { mutableStateOf("1") }
    var expanded by remember { mutableStateOf(false) }

    Card(shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))

            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                OutlinedTextField(
                    value = kinds.find { it.first == selectedKindId }?.second ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    kinds.forEach { (id, name) ->
                        DropdownMenuItem(
                            text = { Text(name) },
                            onClick = { selectedKindId = id; expanded = false }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Qtd.") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    singleLine = true
                )
                Button(onClick = {
                    val qty = quantity.toIntOrNull() ?: return@Button
                    val area = kindAreas[selectedKindId] ?: return@Button
                    onAdd(selectedKindId, qty, area)
                    quantity = "1"
                }) { Text("Adicionar") }
            }

            added.forEachIndexed { index, label ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(label, style = MaterialTheme.typography.bodySmall)
                    IconButton(onClick = { onRemove(index) }) { Text("✕") }
                }
            }
        }
    }
}

private fun RoomKind.label() = when (this) {
    RoomKind.SYMMETRIC -> "Simétrico (retangular)"
    RoomKind.ASYMMETRIC -> "Assimétrico (perímetro irregular)"
    RoomKind.EXTERNAL -> "Externo / Fachada"
}

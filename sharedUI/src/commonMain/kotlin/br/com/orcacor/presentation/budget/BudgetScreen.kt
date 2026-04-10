package br.com.orcacor.presentation.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.orcacor.domain.entity.Room
import br.com.orcacor.util.formatFloat
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    budgetId: String,
    onNavigateToRoomForm: (budgetId: String) -> Unit,
    onNavigateToReport: (budgetId: String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: BudgetViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(budgetId) {
        viewModel.loadBudget(budgetId)
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is BudgetEffect.NavigateToRoomForm -> onNavigateToRoomForm(effect.budgetId)
                is BudgetEffect.NavigateToReport -> onNavigateToReport(effect.budgetId)
            }
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onIntent(BudgetIntent.ClearError)
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(state.budget?.number ?: "") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                state.budget?.let { onNavigateToRoomForm(it.id) }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar cômodo")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.recipientName,
                    onValueChange = { viewModel.onIntent(BudgetIntent.UpdateRecipient(it, state.recipientEmail)) },
                    label = { Text("Nome do profissional de pintura") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = state.recipientEmail,
                    onValueChange = { viewModel.onIntent(BudgetIntent.UpdateRecipient(state.recipientName, it)) },
                    label = { Text("E-mail do profissional") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                    singleLine = true
                )

                Spacer(Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Cômodos", style = MaterialTheme.typography.labelMedium)
                            Text("${state.rooms.size}", style = MaterialTheme.typography.headlineSmall)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Área total", style = MaterialTheme.typography.labelMedium)
                            Text(
                                "${formatFloat(state.budget?.totalArea ?: 0f)} m²",
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(state.rooms) { room ->
                        RoomItem(room = room, onDelete = {
                            room.id?.let { id -> viewModel.onIntent(BudgetIntent.DeleteRoom(id)) }
                        })
                        Spacer(Modifier.height(8.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = { state.budget?.let { onNavigateToRoomForm(it.id) } },
                        modifier = Modifier.weight(1f)
                    ) { Text("+ Cômodo") }

                    Button(
                        onClick = { viewModel.onIntent(BudgetIntent.GenerateReport) },
                        modifier = Modifier.weight(1f),
                        enabled = state.rooms.isNotEmpty()
                    ) { Text("Relatório") }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun RoomItem(room: Room, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(room.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    "${room.kind.name.lowercase().replaceFirstChar { it.uppercase() }} • ${formatFloat(room.totalSquareMeters)} m²",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Remover cômodo")
            }
        }
    }
}
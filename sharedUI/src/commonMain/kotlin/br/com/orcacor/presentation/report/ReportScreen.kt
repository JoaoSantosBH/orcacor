package br.com.orcacor.presentation.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.orcacor.domain.entity.MaterialEstimate
import br.com.orcacor.domain.entity.Report
import br.com.orcacor.domain.entity.Room
import br.com.orcacor.util.formatFloat
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    budgetId: String,
    onNavigateBack: () -> Unit,
    viewModel: ReportViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(budgetId) {
        viewModel.onIntent(ReportIntent.Load(budgetId))
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Relatório") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                state.error != null -> Text(
                    state.error!!,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
                state.report != null -> ReportContent(report = state.report!!)
            }
        }
    }
}

@Composable
private fun ReportContent(report: Report) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(Modifier.height(8.dp)) }

        // Header
        item {
            Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Orçamento ${report.budget.number}", style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.height(4.dp))
                    Text("Destinatário: ${report.budget.recipientName}", style = MaterialTheme.typography.bodyMedium)
                    Text("E-mail: ${report.budget.recipientEmail}", style = MaterialTheme.typography.bodyMedium)
                    Text("Elaborado por: ${report.user.name}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Área total: ${formatFloat(report.totalArea)} m²",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Rooms section
        item {
            Text("Cômodos", style = MaterialTheme.typography.titleMedium)
        }
        items(report.rooms) { room -> RoomReportItem(room) }

        // Materials section
        item {
            Spacer(Modifier.height(8.dp))
            Text("Materiais Estimados", style = MaterialTheme.typography.titleMedium)
        }
        items(report.materials) { material -> MaterialReportItem(material) }

        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun RoomReportItem(room: Room) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(room.name, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Paredes", style = MaterialTheme.typography.labelSmall)
                    Text("${formatFloat(room.wallsArea)} m²", style = MaterialTheme.typography.bodySmall)
                }
                Column {
                    Text("Teto", style = MaterialTheme.typography.labelSmall)
                    Text("${formatFloat(room.ceilingArea)} m²", style = MaterialTheme.typography.bodySmall)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Total líquido", style = MaterialTheme.typography.labelSmall)
                    Text(
                        "${formatFloat(room.totalSquareMeters)} m²",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun MaterialReportItem(material: MaterialEstimate) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(material.name, style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(4.dp))
            Text(
                "${formatFloat(material.totalLiters)} L (Rendimento: ${material.yieldPerLiter} m²/L)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (material.cans18L > 0 || material.cans36L > 0 || material.cans09L > 0) {
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (material.cans18L > 0) Text("${material.cans18L}x 18L", style = MaterialTheme.typography.bodySmall)
                    if (material.cans36L > 0) Text("${material.cans36L}x 3,6L", style = MaterialTheme.typography.bodySmall)
                    if (material.cans09L > 0) Text("${material.cans09L}x 0,9L", style = MaterialTheme.typography.bodySmall)
                }
            } else if (material.cans09L > 0) {
                Text("Quantidade: ${material.cans09L}", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

package br.com.orcacor.presentation.instructions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private data class InstructionStep(val number: String, val title: String, val description: String)

private val steps = listOf(
    InstructionStep("1", "Crie um orçamento", "Acesse a aba Orçamento e toque em 'Novo Orçamento'. Informe o nome e e-mail do profissional de pintura."),
    InstructionStep("2", "Adicione cômodos", "Toque em '+ Cômodo' e preencha o nome, tipo (simétrico, assimétrico ou externo) e as dimensões do cômodo."),
    InstructionStep("3", "Informe acessórios", "Para cada cômodo, adicione portas, janelas, espelhos e armários. As áreas serão descontadas automaticamente."),
    InstructionStep("4", "Calcule automaticamente", "O app calcula a área líquida de pintura em tempo real, considerando teto, paredes e descontos."),
    InstructionStep("5", "Gere o relatório", "Com todos os cômodos adicionados, toque em 'Relatório' para ver o detalhamento completo de materiais."),
    InstructionStep("6", "Compartilhe", "Compartilhe o relatório por e-mail ou WhatsApp diretamente com o profissional de pintura.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstructionsScreen() {
    Scaffold(
        topBar = { LargeTopAppBar(title = { Text("Instruções") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Como usar o OrcaCor",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    "Siga os passos abaixo para gerar seu orçamento de pintura profissional.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(24.dp))
            }

            items(steps.size) { index ->
                val step = steps[index]
                Column {
                    Text(
                        "Passo ${step.number} — ${step.title}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        step.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

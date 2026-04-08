package br.com.orcacor.presentation.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import br.com.orcacor.presentation.budget.CreateBudgetScreen
import br.com.orcacor.presentation.historic.HistoricScreen
import br.com.orcacor.presentation.instructions.InstructionsScreen
import br.com.orcacor.presentation.profile.ProfileScreen

private enum class HomeTab(val label: String) {
    BUDGET("Orçamento"),
    HISTORIC("Histórico"),
    PROFILE("Perfil"),
    INSTRUCTIONS("Instruções")
}

@Composable
fun HomeScreen(
    onNavigateToBudget: (budgetId: String) -> Unit,
    onNavigateToReport: (budgetId: String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var currentTab by rememberSaveable { mutableStateOf(HomeTab.BUDGET) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentTab == HomeTab.BUDGET,
                    onClick = { currentTab = HomeTab.BUDGET },
                    icon = { Icon(Icons.Default.Calculate, contentDescription = null) },
                    label = { Text(HomeTab.BUDGET.label) }
                )
                NavigationBarItem(
                    selected = currentTab == HomeTab.HISTORIC,
                    onClick = { currentTab = HomeTab.HISTORIC },
                    icon = { Icon(Icons.Default.History, contentDescription = null) },
                    label = { Text(HomeTab.HISTORIC.label) }
                )
                NavigationBarItem(
                    selected = currentTab == HomeTab.PROFILE,
                    onClick = { currentTab = HomeTab.PROFILE },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text(HomeTab.PROFILE.label) }
                )
                NavigationBarItem(
                    selected = currentTab == HomeTab.INSTRUCTIONS,
                    onClick = { currentTab = HomeTab.INSTRUCTIONS },
                    icon = { Icon(Icons.Default.Info, contentDescription = null) },
                    label = { Text(HomeTab.INSTRUCTIONS.label) }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AnimatedContent(targetState = currentTab) { tab ->
                when (tab) {
                    HomeTab.BUDGET -> CreateBudgetScreen(
                        onNavigateToBudget = onNavigateToBudget
                    )
                    HomeTab.HISTORIC -> HistoricScreen(
                        onNavigateToReport = onNavigateToReport
                    )
                    HomeTab.PROFILE -> ProfileScreen(
                        onNavigateToLogin = onNavigateToLogin
                    )
                    HomeTab.INSTRUCTIONS -> InstructionsScreen()
                }
            }
        }
    }
}
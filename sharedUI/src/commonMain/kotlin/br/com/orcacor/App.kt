package br.com.orcacor

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import br.com.orcacor.navigation.BudgetDestination
import br.com.orcacor.navigation.HomeDestination
import br.com.orcacor.navigation.LoginDestination
import br.com.orcacor.navigation.OnboardingDestination
import br.com.orcacor.navigation.RegisterDestination
import br.com.orcacor.navigation.ReportDestination
import br.com.orcacor.navigation.RoomFormDestination
import br.com.orcacor.navigation.SplashDestination
import br.com.orcacor.presentation.auth.LoginScreen
import br.com.orcacor.presentation.auth.RegisterScreen
import br.com.orcacor.presentation.budget.BudgetScreen
import br.com.orcacor.presentation.home.HomeScreen
import br.com.orcacor.presentation.onboarding.OnboardingScreen
import br.com.orcacor.presentation.report.ReportScreen
import br.com.orcacor.presentation.room.RoomFormScreen
import br.com.orcacor.presentation.splash.SplashScreen
import br.com.orcacor.theme.AppTheme

@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    var backStack by remember { mutableStateOf(listOf<Any>(SplashDestination)) }

    fun navigate(dest: Any) { backStack = backStack + dest }
    fun navigateBack() { if (backStack.size > 1) backStack = backStack.dropLast(1) }
    fun navigateAndClear(dest: Any) { backStack = listOf(dest) }

    val current = backStack.last()

    AnimatedContent(
        targetState = current,
        transitionSpec = {
            slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
        }
    ) { destination ->
        when (destination) {
            is SplashDestination -> SplashScreen(
                onNavigateToOnboarding = { navigateAndClear(OnboardingDestination) },
                onNavigateToHome = { navigateAndClear(HomeDestination) },
                isLoggedIn = false,
                isFirstLaunch = true
            )
            is OnboardingDestination -> OnboardingScreen(
                onFinish = { navigateAndClear(LoginDestination) }
            )
            is LoginDestination -> LoginScreen(
                onNavigateToHome = { navigateAndClear(HomeDestination) },
                onNavigateToRegister = { navigate(RegisterDestination) }
            )
            is RegisterDestination -> RegisterScreen(
                onNavigateBack = ::navigateBack,
                onNavigateToHome = { navigateAndClear(HomeDestination) }
            )
            is HomeDestination -> HomeScreen(
                onNavigateToBudget = { budgetId -> navigate(BudgetDestination(budgetId)) },
                onNavigateToReport = { budgetId -> navigate(ReportDestination(budgetId)) },
                onNavigateToLogin = { navigateAndClear(LoginDestination) }
            )
            is BudgetDestination -> BudgetScreen(
                budgetId = destination.budgetId,
                onNavigateToRoomForm = { budgetId -> navigate(RoomFormDestination(budgetId)) },
                onNavigateToReport = { budgetId -> navigate(ReportDestination(budgetId)) },
                onNavigateBack = ::navigateBack
            )
            is RoomFormDestination -> RoomFormScreen(
                budgetId = destination.budgetId,
                onNavigateBack = ::navigateBack
            )
            is ReportDestination -> ReportScreen(
                budgetId = destination.budgetId,
                onNavigateBack = ::navigateBack
            )
        }
    }
}
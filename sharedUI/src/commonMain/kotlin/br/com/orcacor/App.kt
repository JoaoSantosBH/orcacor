package br.com.orcacor

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import br.com.orcacor.domain.usecase.auth.GetCurrentUserUseCase
import br.com.orcacor.navigation.HomeDestination
import br.com.orcacor.navigation.LoginDestination
import br.com.orcacor.navigation.OnboardingDestination
import br.com.orcacor.navigation.RegisterDestination
import br.com.orcacor.navigation.ReportDestination
import br.com.orcacor.navigation.RoomFormDestination
import br.com.orcacor.navigation.SplashDestination
import br.com.orcacor.presentation.auth.LoginScreen
import br.com.orcacor.presentation.auth.RegisterScreen
import br.com.orcacor.presentation.home.HomeScreen
import br.com.orcacor.presentation.onboarding.OnboardingScreen
import br.com.orcacor.presentation.report.ReportScreen
import br.com.orcacor.presentation.room.RoomFormScreen
import br.com.orcacor.presentation.splash.SplashScreen
import br.com.orcacor.theme.AppTheme
import com.russhwolf.settings.Settings
import org.koin.compose.koinInject

private const val PREF_FIRST_LAUNCH = "first_launch"

@Composable
fun App(
    onThemeChanged: @Composable (isDark: Boolean) -> Unit = {}
) = AppTheme(onThemeChanged) {
    AppNavigation()
}

@Composable
private fun AppNavigation() {
    val backStack = rememberNavBackStack(SplashDestination)
    val getCurrentUser = koinInject<GetCurrentUserUseCase>()
    val settings = remember { Settings() }

    var isLoggedIn by remember { mutableStateOf(false) }
    var isFirstLaunch by remember { mutableStateOf(settings.getBoolean(PREF_FIRST_LAUNCH, true)) }

    LaunchedEffect(Unit) {
        isLoggedIn = getCurrentUser.invoke() != null
    }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { destination ->
            when (destination) {
                is SplashDestination -> NavEntry(destination) {
                    SplashScreen(
                        onNavigateToOnboarding = {
                            settings.putBoolean(PREF_FIRST_LAUNCH, false)
                            backStack.add(OnboardingDestination)
                        },
                        onNavigateToHome = {
                            backStack.clear()
                            backStack.add(HomeDestination)
                        },
                        isLoggedIn = isLoggedIn,
                        isFirstLaunch = isFirstLaunch
                    )
                }

                is OnboardingDestination -> NavEntry(destination) {
                    OnboardingScreen(
                        onFinish = {
                            backStack.clear()
                            backStack.add(LoginDestination)
                        }
                    )
                }

                is LoginDestination -> NavEntry(destination) {
                    LoginScreen(
                        onNavigateToHome = {
                            backStack.clear()
                            backStack.add(HomeDestination)
                        },
                        onNavigateToRegister = { backStack.add(RegisterDestination) }
                    )
                }

                is RegisterDestination -> NavEntry(destination) {
                    RegisterScreen(
                        onNavigateBack = { backStack.removeLastOrNull() },
                        onNavigateToHome = {
                            backStack.clear()
                            backStack.add(HomeDestination)
                        }
                    )
                }

                is HomeDestination -> NavEntry(destination) {
                    HomeScreen(
                        onNavigateToRoomForm = { budgetId -> backStack.add(RoomFormDestination(budgetId)) },
                        onNavigateToReport = { budgetId -> backStack.add(ReportDestination(budgetId)) },
                        onNavigateToLogin = {
                            backStack.clear()
                            backStack.add(LoginDestination)
                        }
                    )
                }

                is RoomFormDestination -> NavEntry(destination) {
                    RoomFormScreen(
                        budgetId = destination.budgetId,
                        onNavigateBack = { backStack.removeLastOrNull() }
                    )
                }

                is ReportDestination -> NavEntry(destination) {
                    ReportScreen(
                        budgetId = destination.budgetId,
                        onNavigateBack = { backStack.removeLastOrNull() }
                    )
                }

                else -> NavEntry(destination) { }
            }
        }
    )
}

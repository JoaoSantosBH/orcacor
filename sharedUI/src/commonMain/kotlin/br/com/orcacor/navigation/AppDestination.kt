package br.com.orcacor.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable object SplashDestination : NavKey
@Serializable object OnboardingDestination : NavKey
@Serializable object LoginDestination : NavKey
@Serializable object RegisterDestination : NavKey
@Serializable object HomeDestination : NavKey
@Serializable data class RoomFormDestination(val budgetId: String) : NavKey
@Serializable data class ReportDestination(val budgetId: String) : NavKey

// Bottom nav destinations
@Serializable object BudgetTabDestination
@Serializable object HistoricTabDestination
@Serializable object ProfileTabDestination
@Serializable object InstructionsTabDestination

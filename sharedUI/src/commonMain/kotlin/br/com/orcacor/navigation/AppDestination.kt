package br.com.orcacor.navigation

import kotlinx.serialization.Serializable

@Serializable object SplashDestination
@Serializable object OnboardingDestination
@Serializable object LoginDestination
@Serializable object RegisterDestination
@Serializable object HomeDestination
@Serializable data class RoomFormDestination(val budgetId: String)
@Serializable data class ReportDestination(val budgetId: String)

// Bottom nav destinations
@Serializable object BudgetTabDestination
@Serializable object HistoricTabDestination
@Serializable object ProfileTabDestination
@Serializable object InstructionsTabDestination

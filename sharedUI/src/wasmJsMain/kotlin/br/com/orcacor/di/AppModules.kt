package br.com.orcacor.di

import br.com.orcacor.domain.usecase.auth.GetCurrentUserUseCase
import br.com.orcacor.domain.usecase.auth.LoginUseCase
import br.com.orcacor.domain.usecase.auth.LogoutUseCase
import br.com.orcacor.domain.usecase.auth.RegisterUseCase
import br.com.orcacor.domain.usecase.budget.CreateBudgetUseCase
import br.com.orcacor.domain.usecase.budget.DeleteBudgetUseCase
import br.com.orcacor.domain.usecase.budget.GetBudgetHistoryUseCase
import br.com.orcacor.domain.usecase.budget.SaveBudgetUseCase
import br.com.orcacor.domain.usecase.material.CalculateMaterialsUseCase
import br.com.orcacor.domain.usecase.report.GenerateReportUseCase
import br.com.orcacor.domain.usecase.room.AddRoomToBudgetUseCase
import br.com.orcacor.domain.usecase.room.CalculateRoomAreaUseCase
import br.com.orcacor.domain.usecase.room.DeleteRoomUseCase
import br.com.orcacor.presentation.auth.AuthViewModel
import br.com.orcacor.presentation.budget.BudgetViewModel
import br.com.orcacor.presentation.historic.HistoricViewModel
import br.com.orcacor.presentation.profile.ProfileViewModel
import br.com.orcacor.presentation.report.ReportViewModel
import br.com.orcacor.presentation.room.RoomFormViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

// Web (JS): sem banco local — dados vêm da API REST
val dataModule = module { }

val domainModule = module {
    factoryOf(::CreateBudgetUseCase)
    factoryOf(::SaveBudgetUseCase)
    factoryOf(::GetBudgetHistoryUseCase)
    factoryOf(::DeleteBudgetUseCase)
    factoryOf(::CalculateRoomAreaUseCase)
    factoryOf(::AddRoomToBudgetUseCase)
    factoryOf(::DeleteRoomUseCase)
    factoryOf(::CalculateMaterialsUseCase)
    factoryOf(::GenerateReportUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    factoryOf(::LogoutUseCase)
    factoryOf(::GetCurrentUserUseCase)
}

val presentationModule = module {
    viewModelOf(::AuthViewModel)
    viewModelOf(::BudgetViewModel)
    viewModelOf(::RoomFormViewModel)
    viewModelOf(::ReportViewModel)
    viewModelOf(::HistoricViewModel)
    viewModelOf(::ProfileViewModel)
}

val appModules = listOf(dataModule, domainModule, presentationModule)

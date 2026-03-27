package br.com.orcacor.di

import br.com.orcacor.data.local.createDatabase
import br.com.orcacor.data.repository.BudgetRepositoryImpl
import br.com.orcacor.data.repository.MaterialConstantsRepositoryImpl
import br.com.orcacor.data.repository.RoomRepositoryImpl
import br.com.orcacor.data.repository.UserRepositoryImpl
import br.com.orcacor.domain.repository.BudgetRepository
import br.com.orcacor.domain.repository.MaterialConstantsRepository
import br.com.orcacor.domain.repository.RoomRepository
import br.com.orcacor.domain.repository.UserRepository
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
import br.com.orcacor.presentation.budget.BudgetViewModel
import br.com.orcacor.presentation.auth.AuthViewModel
import br.com.orcacor.presentation.historic.HistoricViewModel
import br.com.orcacor.presentation.profile.ProfileViewModel
import br.com.orcacor.presentation.report.ReportViewModel
import br.com.orcacor.presentation.room.RoomFormViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single { createDatabase() }
    single { get<br.com.orcacor.data.local.AppDatabase>().budgetDao() }
    single { get<br.com.orcacor.data.local.AppDatabase>().roomDao() }
    single { get<br.com.orcacor.data.local.AppDatabase>().accessoryDao() }
    single { get<br.com.orcacor.data.local.AppDatabase>().userDao() }

    singleOf(::BudgetRepositoryImpl) bind BudgetRepository::class
    singleOf(::RoomRepositoryImpl) bind RoomRepository::class
    singleOf(::UserRepositoryImpl) bind UserRepository::class
    singleOf(::MaterialConstantsRepositoryImpl) bind MaterialConstantsRepository::class
}

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

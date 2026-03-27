package com.jomar.senhorpintor.modules

import com.jomar.senhorpintor.presentation.historic.HistoricViewModel
import com.jomar.senhorpintor.presentation.rooms.AssimetricRoomViewModel
import com.jomar.senhorpintor.presentation.new_budget.NewBudgetViewModel
import com.jomar.senhorpintor.presentation.register.RegisterViewModel
import com.jomar.senhorpintor.presentation.report.ReportViewModel
import com.jomar.senhorpintor.presentation.rooms.ExternalRoomViewModel
import com.jomar.senhorpintor.presentation.rooms.SimetricRoomViewModel
import com.jomar.senhorpintor.presentation.splash.SplashViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { SplashViewModel(app = get(), preferencesRepository = get()) }
    viewModel { RegisterViewModel(app = get(), preferencesRepository = get(), userInteractor = get()) }
    viewModel { NewBudgetViewModel(app = get(), interactor = get()) }
    viewModel { AssimetricRoomViewModel(app = get(), interactor = get()) }
    viewModel { ExternalRoomViewModel(app = get(), interactor = get()) }
    viewModel { SimetricRoomViewModel(app = get(), interactor = get()) }
    viewModel { HistoricViewModel(app = get(),interactor = get(), interactorR = get()) }
    viewModel { ReportViewModel(app = get(),interactoA = get(),interactorH = get(),interactorM = get(), interactor = get(), userInteractor = get()) }
}
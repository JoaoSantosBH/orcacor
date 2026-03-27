package com.jomar.senhorpintor.modules


import com.jomar.senhorpintor.data.local.interactor.acessory.AcessoryInteractor
import com.jomar.senhorpintor.data.local.interactor.acessory.AcessoryInteractorImpl
import com.jomar.senhorpintor.data.local.interactor.budget.preferences.PreferencesInteractor
import com.jomar.senhorpintor.data.local.interactor.budget.preferences.PreferencesInteractorImpl
import com.jomar.senhorpintor.data.local.interactor.budget.user.UserInteractor
import com.jomar.senhorpintor.data.local.interactor.budget.user.UserInteractorImpl
import com.jomar.senhorpintor.data.local.interactor.budget.BudgetInteractor
import com.jomar.senhorpintor.data.local.interactor.budget.BudgetInteractorImpl
import com.jomar.senhorpintor.data.local.interactor.header.HeaderInteractor
import com.jomar.senhorpintor.data.local.interactor.header.HeaderInteractorImpl
import com.jomar.senhorpintor.data.local.interactor.material.MaterialInteractor
import com.jomar.senhorpintor.data.local.interactor.material.MaterialInteractorImpl
import com.jomar.senhorpintor.data.local.interactor.rooms.RoomInteractor
import com.jomar.senhorpintor.data.local.interactor.rooms.RoomInteractorImpl
import org.koin.dsl.module.module

val interactorModule = module {

//
    factory { PreferencesInteractorImpl(get()) as PreferencesInteractor }
    factory { UserInteractorImpl(get()) as UserInteractor }
    factory { BudgetInteractorImpl(get()) as BudgetInteractor }
    factory { RoomInteractorImpl(get()) as RoomInteractor }
    factory { AcessoryInteractorImpl(get()) as AcessoryInteractor }
    factory { HeaderInteractorImpl(get()) as HeaderInteractor }
    factory { MaterialInteractorImpl(get()) as MaterialInteractor}
//
//    factory { FeedInteractorImpl(get(), get()) as FeedInteractor }
//
//    factory { EventInteractorImpl(get(), get()) as EventInteractor }
//
//    factory { PublicationInteractorImpl(get(), get()) as PublicationInteractor }
//
//    factory { PlaylistInteractorImpl(get(), get(), get()) as PlaylistInteractor }
//
//    factory { PdfViewerInteractorImpl(get(), get()) as PdfViewerInteractor }
//
//    factory { PlaylistItemInteractorImpl(get(), get(), get()) as PlaylistItemInteractor }
//
//    factory { ProfileInteractorImpl(get(), get(), get(), get()) as ProfileInteractor }
//
//    factory { FavoriteInteractorImpl(get(), get()) as FavoriteInteractor }
//
//    factory { NotificationsInteractorImpl(get(), get(), get(), get(), get()) as NotificationsInteractor }
//
//    factory { ProgramInteractorImpl(get(), get()) as ProgramInteractor }
//
//    factory { MyNetworkInteractorImpl(get(), get(), get()) as MyNetworkInteractor }
//
//    factory { SuggestedNetworkInteractorImpl(get(), get(), get()) as SuggestedNetworkInteractor }
//
//    factory { ContactProfileInteractorImpl(get(), get(), get()) as ContactProfileInteractor }
//
//    factory { SearchNetworkInteractorImpl(get(), get(), get()) as SearchNetworkInteractor }
//
//    factory { SearchAllNetworkInteractorImpl(get(), get()) as SearchAllNetworkInteractor }
//
//    factory { PendingNetworkInteractorImpl(get(), get()) as PendingNetworkInteractor }
//
//    factory { ProgramNetworkInteractorImpl(get(), get()) as ProgramsNetworkInteractor }
//
//    factory { InitialPlaylistItemInteractorImpl(get(), get()) as InitialPlaylistItemInteractor }
//
//    factory { FinalPlaylistItemInteractorImpl(get(), get(), get()) as FinalPlaylistItemInteractor }
//
//    factory { AcceptTermsInteractorImpl(get(), get(), get()) as AcceptTermsInteractor }
//
//    factory { ProgramDiscussionInteractorImpl(get(), get(), get()) as ProgramDiscussionInteractor }
//
//    factory { AdvancedSearchInteractorImpl(get(), get()) as AdvancedSearchInteractor }
}
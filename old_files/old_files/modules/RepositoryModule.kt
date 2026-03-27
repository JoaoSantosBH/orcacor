package com.jomar.senhorpintor.modules

import com.jomar.senhorpintor.data.local.interactor.acessory.AcessoryRepository
import com.jomar.senhorpintor.data.local.interactor.acessory.AcessoryRepositoryImpl
import com.jomar.senhorpintor.data.local.interactor.budget.BudgetRepository
import com.jomar.senhorpintor.data.local.interactor.budget.BudgetRepositoryImpl
import com.jomar.senhorpintor.data.local.interactor.budget.preferences.PreferencesRepository
import com.jomar.senhorpintor.data.local.interactor.budget.preferences.PreferencesRepositoryImpl
import com.jomar.senhorpintor.data.local.interactor.budget.user.UserRepository
import com.jomar.senhorpintor.data.local.interactor.budget.user.UserRepositoryImpl
import com.jomar.senhorpintor.data.local.interactor.header.HeaderRepository
import com.jomar.senhorpintor.data.local.interactor.header.HeaderRepositoryImpl
import com.jomar.senhorpintor.data.local.interactor.material.MaterialRepository
import com.jomar.senhorpintor.data.local.interactor.material.MaterialRepositoryImpl
import com.jomar.senhorpintor.data.local.interactor.rooms.RoomRepository
import com.jomar.senhorpintor.data.local.interactor.rooms.RoomsRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module

val repositoryModule = module {

    factory { PreferencesRepositoryImpl(androidContext()) as PreferencesRepository }
    factory { UserRepositoryImpl(androidContext(), get()) as UserRepository }
    factory { BudgetRepositoryImpl(androidContext(), get()) as BudgetRepository }
    factory { RoomsRepositoryImpl(androidContext(), get()) as RoomRepository }
    factory { AcessoryRepositoryImpl(androidContext(), get()) as AcessoryRepository }
    factory { HeaderRepositoryImpl(androidContext(), get()) as HeaderRepository }
    factory { MaterialRepositoryImpl(androidContext(),get()) as MaterialRepository }


//    factory { FeedRepositoryImpl(get()) as FeedRepository }
//
//    factory { EventRepositoryImpl(get()) as EventRepository }
//
//    factory { PublicationRepositoryImpl(get(), get(), get()) as PublicationRepository }
//
//    factory { PlaylistRepositoryImpl(get(), get()) as PlaylistRepository }
//
//    factory { PdfViewerRepositoryImpl(get(), get()) as PdfViewerRepository }
//
//    factory { PlaylistItemRepositoryImpl(get(), get()) as PlaylistItemRepository }
//
//    factory { ProfileRepositoryImpl(get()) as ProfileRepository }
//
//    factory { FavoriteRepositoryImpl(get()) as FavoriteRepository }
//
//    factory { NotificationsRepositoryImpl(get(), get()) as NotificationsRepository }
//
//    factory { ProgramRepositoryImpl(get()) as ProgramRepository }
//
//    factory { MyNetworkRepositoryImpl(get()) as MyNetworkRepository }
//
//    factory { SuggestedNetworkRepositoryImpl(get()) as SuggestedNetworkRepository }
//
//    factory { ContactProfileRepositoryImpl(get(), get(), get()) as ContactProfileRepository }
//
//    factory { SearchNetworkRepositoryImpl(get()) as SearchNetworkRepository }
//
//    factory { SearchAllNetworkRepositoryImpl(get()) as SearchAllNetworkrRepository }
//
//    factory { PendingNetworkRepositoryImpl(get()) as PendingNetworkRepository }
//
//    factory { ProgramNetworkRepositoryImpl(get()) as ProgramNetworkRepository }
//
//    factory { AcceptTermsRepositoryImpl(get()) as AcceptTermsRepository }
//
//    factory { ProgramDiscussionRepositoryImpl(get(), get()) as ProgramDiscussionRepository }
//
//    factory { AdvancedSearchRepositoryImpl(get()) as AdvancedSearchRepository }
}
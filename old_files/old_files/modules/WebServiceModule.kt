package com.jomar.senhorpintor.modules

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jomar.senhorpintor.service.RequestInterceptor
import com.jomar.senhorpintor.service.ServiceClientFactory
import org.koin.dsl.module.module

val webServiceModule = module {

//    single {
//        createClient<PodcastWebService>(
//                BuildConfig.PODCAST_URL ,
//                get(),
//                get()
//        )
//    }
//    single {
//        createClient<FeedWebService>(
//                BuildConfig.PODCAST_URL ,
//                get(),
//                get()
//        )
//    }
    single { ServiceClientFactory.createHttpLoggingInterceptor() }
    single { ServiceClientFactory.createOkHttpClient(get()) }
    factory { CoroutineCallAdapterFactory() }
    single { RequestInterceptor() }

}
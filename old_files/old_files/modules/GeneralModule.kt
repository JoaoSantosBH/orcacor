package com.jomar.senhorpintor.modules

import com.jomar.senhorpintor.base.ResourcesReferences
import com.jomar.senhorpintor.util.ThreadContextProvider
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module

val generalModule = module {

    factory { ThreadContextProvider() }
    single { ResourcesReferences(androidApplication()) }

}
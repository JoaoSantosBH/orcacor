package com.jomar.senhorpintor.base

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.jomar.senhorpintor.modules.*
import org.koin.android.ext.android.startKoin


open class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        instance = this
        startKoin(
            this,
            listOf(generalModule, viewModelModule, interactorModule, repositoryModule, webServiceModule, dbModule)
        )
    }

    companion object {
        lateinit var instance: App
            private set
    }
}   
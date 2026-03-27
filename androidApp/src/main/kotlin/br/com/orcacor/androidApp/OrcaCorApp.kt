package br.com.orcacor.androidApp

import android.app.Application
import br.com.orcacor.data.local.androidContext
import br.com.orcacor.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class OrcaCorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        androidContext = applicationContext
        startKoin {
            androidContext(applicationContext)
            modules(appModules)
        }
    }
}

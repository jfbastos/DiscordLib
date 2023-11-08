package br.com.maximatech.discordlogger.application

import android.app.Application
import br.com.maximatech.discordlogger.di.dispatcherModule
import br.com.maximatech.discordlogger.di.repositoryModule
import br.com.maximatech.discordlogger.di.retrofitModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LibApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@LibApplication)
            modules(listOf(retrofitModule, dispatcherModule, repositoryModule))
        }
    }
}
package com.mylektop.awesomeapp.app

import androidx.multidex.MultiDexApplication
import com.mylektop.awesomeapp.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Main Application class.
 * Dependency Injection initiated for all sub modules.
 * Created by iddangunawan on 12/12/20
 */
open class MainApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initiateKoin()
    }

    private fun initiateKoin() {
        startKoin {
            androidContext(this@MainApp)
            modules(provideDependency())
        }
    }

    open fun provideDependency() = appComponent
}
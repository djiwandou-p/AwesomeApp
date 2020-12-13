package com.mylektop.awesomeapp.di

import com.mylektop.awesomeapp.platform.SharedPreferenceHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Shared Preference DI Module.
 * Created by iddangunawan on 12/13/20
 */
val SharedPrefDependency = module {

    factory { SharedPreferenceHelper(androidContext()) }
}
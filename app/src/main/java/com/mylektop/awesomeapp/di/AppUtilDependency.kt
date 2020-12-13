package com.mylektop.awesomeapp.di

import com.mylektop.awesomeapp.utils.AppUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * DI module for App Util dependency.
 * Created by iddangunawan on 12/13/20
 */
val AppUtilDependency = module {

    factory { AppUtils(androidContext()) }
}
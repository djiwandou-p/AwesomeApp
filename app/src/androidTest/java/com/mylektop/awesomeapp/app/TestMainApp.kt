package com.mylektop.awesomeapp.app

import org.koin.core.module.Module

/**
 * Created by iddangunawan on 12/14/20
 */
class TestMainApp : MainApp() {
    override fun provideDependency() = listOf<Module>()
}
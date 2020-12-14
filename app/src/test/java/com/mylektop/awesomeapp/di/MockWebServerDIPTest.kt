package com.mylektop.awesomeapp.di

import okhttp3.mockwebserver.MockWebServer
import org.koin.dsl.module

/**
 * Creates Mockwebserver instance for testing
 * Created by iddangunawan on 12/14/20
 */
val MockWebServerDIPTest = module {
    factory {
        MockWebServer()
    }
}
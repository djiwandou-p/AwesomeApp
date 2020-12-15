package com.mylektop.awesomeapp.di

import okhttp3.mockwebserver.MockWebServer
import org.koin.dsl.module

/**
 * Mock web server Koin DI component for Instrumentation Testing
 * Created by iddangunawan on 12/15/20
 */
val MockWebServerInstrumentationTest = module {
    factory {
        MockWebServer()
    }
}
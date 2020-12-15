package com.mylektop.awesomeapp.di

/**
 * Main Koin DI component for Instrumentation Testing
 * Created by iddangunawan on 12/14/20
 */
fun generateTestAppComponent(baseApi: String) = listOf(
    configureNetworkForInstrumentationTest(baseApi),
    UseCaseDependency,
    MockWebServerInstrumentationTest,
    RepoDependency
)
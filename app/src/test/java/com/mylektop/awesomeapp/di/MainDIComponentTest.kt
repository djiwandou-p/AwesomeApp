package com.mylektop.awesomeapp.di

/**
 * Main Koin DI component.
 * Helps to configure
 * 1) Mockwebserver
 * 2) Usecase
 * 3) Repository
 * Created by iddangunawan on 12/14/20
 */
fun configureTestAppComponent(baseApi: String) = listOf(
    MockWebServerDIPTest,
    configureNetworkModuleForTest(baseApi),
    UseCaseDependency,
    RepoDependency
)


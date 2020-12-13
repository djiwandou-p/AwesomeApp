package com.mylektop.awesomeapp.di

/**
 * Main dependency component.
 * This will create and provide required dependencies with sub dependencies.
 * Created by iddangunawan on 12/12/20
 */
val appComponent = listOf(
    UseCaseDependency,
    AppUtilDependency,
    NetworkDependency,
    SharedPrefDependency,
    RepoDependency
)
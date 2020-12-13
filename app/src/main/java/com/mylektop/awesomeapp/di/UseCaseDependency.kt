package com.mylektop.awesomeapp.di

import com.mylektop.awesomeapp.screens.curated.CuratedUseCase
import org.koin.dsl.module

/**
 * Use case DI module.
 * Provide Use case dependency.
 * Created by iddangunawan on 12/13/20
 */
val UseCaseDependency = module {

    factory { CuratedUseCase() }
}
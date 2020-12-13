package com.mylektop.awesomeapp.di

import com.mylektop.awesomeapp.repository.CuratedRepository
import org.koin.dsl.module

/**
 * Repository DI module.
 * Provides Repo dependency.
 * Created by iddangunawan on 12/13/20
 */
val RepoDependency = module {

    factory { CuratedRepository() }
}
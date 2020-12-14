package com.mylektop.awesomeapp.di

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mylektop.awesomeapp.network.curated.CuratedAPIService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Network module test configuration with mockserver url.
 * Created by iddangunawan on 12/14/20
 */
fun configureNetworkModuleForTest(baseApi: String) = module {
    single {
        Retrofit.Builder()
            .baseUrl(baseApi)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    factory { get<Retrofit>().create(CuratedAPIService::class.java) }
}
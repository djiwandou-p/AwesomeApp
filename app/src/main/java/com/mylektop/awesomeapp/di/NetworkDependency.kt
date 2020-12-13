package com.mylektop.awesomeapp.di

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.mylektop.awesomeapp.BuildConfig
import com.mylektop.awesomeapp.constants.API_KEY
import com.mylektop.awesomeapp.constants.AUTHORIZATION
import com.mylektop.awesomeapp.constants.TIME_OUT
import com.mylektop.awesomeapp.network.curated.CuratedAPIService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Network dependency module.
 * Provides Retrofit dependency with OkHttp logger.
 * Created by iddangunawan on 12/13/20
 */
val NetworkDependency = module {

    single {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val requestBuilder = chain.request().newBuilder()
                    requestBuilder.header(AUTHORIZATION, API_KEY)

                    return chain.proceed(requestBuilder.build())
                }
            })
            .build()

        Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BuildConfig.BASE_URL).build()
    }

    factory { get<Retrofit>().create(CuratedAPIService::class.java) }
}
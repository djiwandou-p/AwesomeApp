package com.mylektop.awesomeapp.screens.curated

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mylektop.awesomeapp.base.BaseUTTest
import com.mylektop.awesomeapp.di.configureTestAppComponent
import com.mylektop.awesomeapp.network.curated.CuratedAPIService
import com.mylektop.awesomeapp.repository.CuratedRepository
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.test.inject
import java.net.HttpURLConnection

/**
 * Created by iddangunawan on 12/14/20
 */
@RunWith(JUnit4::class)
class CuratedUseCaseTest : BaseUTTest() {

    //Target
    private lateinit var mCuratedUseCase: CuratedUseCase

    //Inject login repo created with koin
    val mCuratedRepo: CuratedRepository by inject()

    //Inject api service created with koin
    val mAPIService: CuratedAPIService by inject()

    //Inject Mockwebserver created with koin
    val mockWebServer: MockWebServer by inject()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    val mParamPerPage = 15
    val mParamPage = 1
    val mTotalResults = 8000
    val mNextPage = "https://api.pexels.com/v1/curated/?page=2&per_page=15"

    @Before
    fun start() {
        super.setUp()

        startKoin { modules(configureTestAppComponent(getMockWebServerUrl())) }
    }

    @Test
    fun test_curated_use_case_returns_expected_value() = runBlocking {
        mockNetworkResponseWithFileContent("success_list_response.json", HttpURLConnection.HTTP_OK)
        mCuratedUseCase = CuratedUseCase()

        val dataReceived = mCuratedUseCase.processCuratedUseCase(mParamPerPage, mParamPage)

        assertNotNull(dataReceived)
        assertEquals(dataReceived.total_results, mTotalResults)
        assertEquals(dataReceived.next_page, mNextPage)
    }
}
package com.mylektop.awesomeapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mylektop.awesomeapp.base.BaseUTTest
import com.mylektop.awesomeapp.di.configureTestAppComponent
import com.mylektop.awesomeapp.network.curated.CuratedAPIService
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
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
class CuratedRepositoryTest : BaseUTTest() {

    //Target
    private lateinit var mRepo: CuratedRepository

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
    fun test_curated_repo_retrieves_expected_data() = runBlocking<Unit> {
        mockNetworkResponseWithFileContent("success_list_response.json", HttpURLConnection.HTTP_OK)
        mRepo = CuratedRepository()

        val dataReceived = mRepo.getCuratedPhotoData(mParamPerPage, mParamPage)

        assertNotNull(dataReceived)
        assertEquals(dataReceived.per_page, 15)
        assertEquals(dataReceived.total_results, mTotalResults)
        assertEquals(dataReceived.next_page, mNextPage)
    }
}
package com.mylektop.awesomeapp.screens.curated

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.mylektop.awesomeapp.base.BaseUTTest
import com.mylektop.awesomeapp.di.configureTestAppComponent
import com.mylektop.awesomeapp.models.curated.CuratedPhoto
import com.mylektop.awesomeapp.platform.LiveDataWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin

/**
 * Created by iddangunawan on 12/14/20
 */
@RunWith(JUnit4::class)
class CuratedActivityViewModelTest : BaseUTTest() {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var mCuratedActivityViewModel: CuratedActivityViewModel

    val mDispatcher = Dispatchers.Unconfined

    @MockK
    lateinit var mCuratedUseCase: CuratedUseCase

    val mParamPerPage = 15
    val mParamPage = 1
    val mTotalResults = 8000
    val mNextPage = "https://api.pexels.com/v1/curated/?page=2&per_page=15"

    @Before
    fun start() {
        super.setUp()

        //Used for initiation of Mockk
        MockKAnnotations.init(this)

        //Start Koin with required dependencies
        startKoin { modules(configureTestAppComponent(getMockWebServerUrl())) }
    }

    @Test
    fun test_curated_view_model_data_populates_expected_value() {

        mCuratedActivityViewModel =
            CuratedActivityViewModel(mDispatcher, mDispatcher, mCuratedUseCase)

        val sampleResponse = getJson("success_list_response.json")
        val jsonObj = Gson().fromJson(sampleResponse, CuratedPhoto::class.java)

        //Make sure curated use case returns expected response when called
        coEvery { mCuratedUseCase.processCuratedUseCase(any(), any()) } returns jsonObj
        mCuratedActivityViewModel.mCuratedPhotoResponse.observeForever { }

        mCuratedActivityViewModel.requestCuratedActivityData(mParamPerPage, mParamPage)

        assert(mCuratedActivityViewModel.mCuratedPhotoResponse.value != null)
        assert(mCuratedActivityViewModel.mCuratedPhotoResponse.value!!.responseStatus == LiveDataWrapper.RESPONSESTATUS.SUCCESS)
        val testResult =
            mCuratedActivityViewModel.mCuratedPhotoResponse.value as LiveDataWrapper<CuratedPhoto>
        Assert.assertEquals(testResult.response!!.total_results, mTotalResults)
        Assert.assertEquals(testResult.response!!.next_page, mNextPage)
    }
}
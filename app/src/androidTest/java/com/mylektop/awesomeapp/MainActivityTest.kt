package com.mylektop.awesomeapp

import android.os.SystemClock
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.mylektop.awesomeapp.base.BaseUITest
import com.mylektop.awesomeapp.di.generateTestAppComponent
import com.mylektop.awesomeapp.helpers.recyclerItemAtPosition
import com.mylektop.awesomeapp.screens.curated.CuratedRecyclerViewAdapter
import com.mylektop.awesomeapp.screens.curated.CuratedUseCase
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.test.inject
import java.net.HttpURLConnection

/**
 * Created by iddangunawan on 12/14/20
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest : BaseUITest() {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java, true, false)

    //Inject curated use case created with koin
    val mCuratedUseCase: CuratedUseCase by inject()

    //Inject Mockwebserver created with koin
    val mMockWebServer: MockWebServer by inject()

    val mPhotographerTestOne = "C Technical"
    val mPhotographerUrlTestOne = "https://www.pexels.com/@cotton-technical"
    val mPhotographerTestTwo = "Julia Volk"
    val mPhotographerUrlTestTWO = "https://www.pexels.com/@julia-volk"

    @Before
    fun start() {
        super.setUp()
        loadKoinModules(generateTestAppComponent(getMockWebServerUrl()).toMutableList())
    }

    @Test
    fun test_recyclerview_elements_for_expected_response() {
        mActivityTestRule.launchActivity(null)

        mockNetworkResponseWithFileContent("success_list_response.json", HttpURLConnection.HTTP_OK)

        //Wait for MockWebServer to get back with response
        SystemClock.sleep(1000)

        //Check if item at 0th position is having 0th element in json
        Espresso.onView(withId(R.id.landingListRecyclerView))
            .check(
                ViewAssertions.matches(
                    recyclerItemAtPosition(
                        0,
                        ViewMatchers.hasDescendant(ViewMatchers.withText(mPhotographerTestOne))
                    )
                )
            )

        Espresso.onView(withId(R.id.landingListRecyclerView))
            .check(
                ViewAssertions.matches(
                    recyclerItemAtPosition(
                        0,
                        ViewMatchers.hasDescendant(ViewMatchers.withText(mPhotographerUrlTestOne))
                    )
                )
            )

        //Scroll to last index in json
        Espresso.onView(withId(R.id.landingListRecyclerView)).perform(
            RecyclerViewActions.scrollToPosition<CuratedRecyclerViewAdapter.CuratedFragViewHolder>(
                14
            )
        )

        //Check if item at 14th position is having 14th element in json
        Espresso.onView(withId(R.id.landingListRecyclerView))
            .check(
                ViewAssertions.matches(
                    recyclerItemAtPosition(
                        14,
                        ViewMatchers.hasDescendant(ViewMatchers.withText(mPhotographerTestTwo))
                    )
                )
            )

        Espresso.onView(withId(R.id.landingListRecyclerView))
            .check(
                ViewAssertions.matches(
                    recyclerItemAtPosition(
                        14,
                        ViewMatchers.hasDescendant(ViewMatchers.withText(mPhotographerUrlTestTWO))
                    )
                )
            )

    }
}
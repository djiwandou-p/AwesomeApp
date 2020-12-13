package com.mylektop.awesomeapp.repository

import com.mylektop.awesomeapp.models.curated.CuratedPhoto
import com.mylektop.awesomeapp.network.curated.CuratedAPIService
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Repository for Curated Flow.
 * Requests data from either Network or DB.
 * Created by iddangunawan on 12/13/20
 */
class CuratedRepository : KoinComponent {

    val mCuratedAPIService: CuratedAPIService by inject()

    /**
     * Request data from backend
     */
    suspend fun getCuratedPhotoData(perPage: Int, page: Int): CuratedPhoto {

        return processDataFetchLogic(perPage, page)
    }

    suspend fun processDataFetchLogic(perPage: Int, page: Int): CuratedPhoto {

        for (x in 0 until 3) {
            println(" Data manipulation prior to REST API request if required $x")
        }

        val dataReceived = mCuratedAPIService.getCuratedPhotoData(perPage, page)

        for (x in 0 until 3) {
            println(" Data manipulation post REST API request if required $x")
        }

        return dataReceived
    }
}
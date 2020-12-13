package com.mylektop.awesomeapp.screens.curated

import com.mylektop.awesomeapp.models.curated.CuratedPhoto
import com.mylektop.awesomeapp.repository.CuratedRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * Use Case class for handling Curated flow.
 * Requests data from Repo.
 * Process received data into required model and reverts back to ViewModel.
 * Created by iddangunawan on 12/13/20
 */
class CuratedUseCase : KoinComponent {

    val mCuratedRepo: CuratedRepository by inject()

    suspend fun processCuratedUseCase(perPage: Int, page: Int): CuratedPhoto {
        for (x in 0 until 3) {
            println(" Pre Data manipulation $x")
        }

        val response = mCuratedRepo.getCuratedPhotoData(perPage, page)

        for (x in 0 until 3) {
            println(" Post Data manipulation $x")
        }

        return response
    }
}
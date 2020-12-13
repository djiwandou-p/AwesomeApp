package com.mylektop.awesomeapp.network.curated

import com.mylektop.awesomeapp.models.curated.CuratedPhoto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Curated photo service Retrofit API.
 * Created by iddangunawan on 12/13/20
 */
interface CuratedAPIService {
    @GET("curated/")
    suspend fun getCuratedPhotoData(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): CuratedPhoto
}
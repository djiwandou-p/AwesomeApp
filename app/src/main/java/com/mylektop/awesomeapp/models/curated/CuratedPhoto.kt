package com.mylektop.awesomeapp.models.curated

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by iddangunawan on 12/13/20
 */
data class CuratedPhoto(
    @JsonProperty("page")
    val page: Int,

    @JsonProperty("per_page")
    val per_page: Int,

    @JsonProperty("photos")
    val photos: List<CuratedPhotoResult>,

    @JsonProperty("total_results")
    val total_results: Int,

    @JsonProperty("next_page")
    val next_page: String
)
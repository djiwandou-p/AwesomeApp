package com.mylektop.awesomeapp.models.curated

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by iddangunawan on 12/13/20
 */
data class CuratedPhotoResult(
    @JsonProperty("id")
    val id: Int,

    @JsonProperty("width")
    val width: Int,

    @JsonProperty("height")
    val height: Int,

    @JsonProperty("url")
    val url: String,

    @JsonProperty("photographer")
    val photographer: String,

    @JsonProperty("photographer_url")
    val photographer_url: String,

    @JsonProperty("photographer_id")
    val photographerId: Int,

    @JsonProperty("avg_color")
    val avg_color: String,

    @JsonProperty("src")
    val src: CuratedPhotoResultSrc,

    @JsonProperty("liked")
    val liked: Boolean
)
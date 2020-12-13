package com.mylektop.awesomeapp.models.curated

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by iddangunawan on 12/13/20
 */
data class CuratedPhotoResultSrc(
    @JsonProperty("original")
    val original: String,

    @JsonProperty("large2x")
    val large2x: String,

    @JsonProperty("large")
    val large: String,

    @JsonProperty("medium")
    val medium: String,

    @JsonProperty("small")
    val small: String,

    @JsonProperty("portrait")
    val portrait: String,

    @JsonProperty("landscape")
    val landscape: String,

    @JsonProperty("tiny")
    val tiny: String
)
package com.flourenco.movieschallenge.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchShowsResponseModel(
    @field:Json(name = "results") val results: List<SearchShowModel>?
)

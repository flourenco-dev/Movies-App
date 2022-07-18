package com.flourenco.movieschallenge.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrendingShowsResponseModel(
    @field:Json(name = "items") val items: List<TrendingShowModel>?
)

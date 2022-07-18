package com.flourenco.movieschallenge.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchShowModel(
    @field:Json(name = "id") val id: String?,
    @field:Json(name = "title") val title: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "image") val image: String?,
    @field:Json(name = "runtimeStr") val runtimeStr: String?,
    @field:Json(name = "plot") val plot: String?,
    @field:Json(name = "imDbRating") val imDbRating: String?,
    @field:Json(name = "genres") val genres: String?,
    @field:Json(name = "stars") val stars: String?
)

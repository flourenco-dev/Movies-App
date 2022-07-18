package com.flourenco.movieschallenge.model

import com.google.gson.Gson

data class Detail(
    val id: String,
    val name: String,
    val imagerUrl: String? = null,
    val detailInfos: List<DetailInfo> = emptyList(),
    val isFavorite: Boolean = false,
    val isHidden: Boolean = false
)

fun Detail?.toJsonString(): String? = Gson().toJson(this)

fun String?.toDetail(): Detail? = Gson().fromJson(this, Detail::class.java)
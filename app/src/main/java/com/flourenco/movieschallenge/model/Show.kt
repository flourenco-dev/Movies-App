package com.flourenco.movieschallenge.model

data class Show(
    val id: String,
    val name: String,
    val imagerUrl: String? = null,
    val description: String? = null,
    val rating: String? = null,
    val duration: String? = null,
    val genres: String? = null,
    val stars: String? = null
)

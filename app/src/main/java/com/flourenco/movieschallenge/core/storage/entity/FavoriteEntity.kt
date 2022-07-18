package com.flourenco.movieschallenge.core.storage.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val name: String,
    val imagerUrl: String? = null,
    val description: String? = null,
    val rating: String? = null,
    val duration: String? = null,
    val genres: String? = null,
    val stars: String? = null
)
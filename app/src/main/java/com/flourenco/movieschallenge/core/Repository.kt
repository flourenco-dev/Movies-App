package com.flourenco.movieschallenge.core

import androidx.lifecycle.LiveData
import com.flourenco.movieschallenge.model.Detail
import com.flourenco.movieschallenge.model.Show

interface Repository {
    fun getFavoritesObservable(): LiveData<List<Show>>
    suspend fun addFavorite(detail: Detail)
    suspend fun deleteFavorite(detail: Detail)
    fun addToHiddenShows(showId: String)
    fun removeFromHiddenShows(showId: String)
    fun removeAllHiddenShows()
    suspend fun getDetailByShowId(showId: String): Detail
    suspend fun searchShowsByName(name: String): List<Show>
    suspend fun getTrendingShows(): List<Show>
}
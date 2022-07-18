package com.flourenco.movieschallenge.core.storage

import androidx.lifecycle.LiveData
import com.flourenco.movieschallenge.core.storage.entity.FavoriteEntity

interface StorageHelper {
    fun getToken(): String?
    fun getFavorites(): LiveData<List<FavoriteEntity>>
    suspend fun isFavorite(favoriteId: String): Boolean
    suspend fun addFavorite(favorite: FavoriteEntity): Boolean
    suspend fun deleteFavorite(favorite: FavoriteEntity): Boolean
    fun getHiddenShowIds(): List<String>
    fun addHiddenShowId(showId: String)
    fun removeHiddenShowId(showId: String)
    fun removeAllHiddenShowIds()
}
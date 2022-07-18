package com.flourenco.movieschallenge.core.storage

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.flourenco.movieschallenge.core.storage.dao.FavoritesDao
import com.flourenco.movieschallenge.core.storage.entity.FavoriteEntity
import com.flourenco.movieschallenge.utils.get
import com.flourenco.movieschallenge.utils.hiddenShowsKey

internal class StorageHelperImpl(
    private val preferences: SharedPreferences,
    private val favoritesDao: FavoritesDao
): StorageHelper {

    override fun getToken(): String? =
        preferences.getString(hiddenShowsKey, null)

    override fun getFavorites(): LiveData<List<FavoriteEntity>> = favoritesDao.getFavorites()

    override suspend fun isFavorite(favoriteId: String): Boolean = favoritesDao.contains(favoriteId)

    override suspend fun addFavorite(favorite: FavoriteEntity): Boolean =
        favoritesDao.insertOrUpdate(favorite)

    override suspend fun deleteFavorite(favorite: FavoriteEntity): Boolean =
        favoritesDao.delete(favorite) > -1

    override fun getHiddenShowIds(): List<String> =
        preferences.getStringSet(hiddenShowsKey, null)?.toList() ?: emptyList()

    override fun addHiddenShowId(showId: String) {
        preferences.getStringSet(hiddenShowsKey, null).also {
            val set = it ?: hashSetOf()
            set.add(showId)
            preferences.edit().putStringSet(hiddenShowsKey, set).apply()
        }
    }

    override fun removeHiddenShowId(showId: String) {
        preferences.getStringSet(hiddenShowsKey, null).also {
            val set = it ?: hashSetOf()
            if (set.contains(showId).get()) {
                set.remove(showId)
                preferences.edit().putStringSet(hiddenShowsKey, set).apply()
            }
        }
    }

    override fun removeAllHiddenShowIds() {
        preferences.edit().remove(hiddenShowsKey).apply()
    }
}
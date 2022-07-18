package com.flourenco.movieschallenge.core.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.flourenco.movieschallenge.core.storage.dao.FavoritesDao
import com.flourenco.movieschallenge.core.storage.entity.FavoriteEntity

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract val favoritesDao: FavoritesDao
}
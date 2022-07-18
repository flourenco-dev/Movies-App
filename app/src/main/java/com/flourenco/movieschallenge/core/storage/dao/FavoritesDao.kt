package com.flourenco.movieschallenge.core.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.flourenco.movieschallenge.core.storage.entity.FavoriteEntity

@Dao
abstract class FavoritesDao {

    @Query("SELECT * FROM FavoriteEntity")
    abstract fun getFavorites(): LiveData<List<FavoriteEntity>>

    @Query("SELECT EXISTS (SELECT 1 FROM FavoriteEntity WHERE id = :favoriteId)")
    abstract suspend fun contains(favoriteId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(favorite: FavoriteEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun update(favorite: FavoriteEntity): Int

    @Transaction
    open suspend fun insertOrUpdate(favorite: FavoriteEntity): Boolean {
        return insert(favorite) > -1 || update(favorite) > -1
    }

    @Query("DELETE FROM FavoriteEntity WHERE id = :favoriteId")
    abstract suspend fun deleteById(favoriteId: String): Int

    @Delete
    abstract suspend fun delete(favorite: FavoriteEntity): Int
}
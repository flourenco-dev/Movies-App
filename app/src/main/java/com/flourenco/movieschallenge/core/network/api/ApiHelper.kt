package com.flourenco.movieschallenge.core.network.api

import com.flourenco.movieschallenge.core.network.model.DetailModel
import com.flourenco.movieschallenge.core.network.model.SearchShowModel
import com.flourenco.movieschallenge.core.network.model.TrendingShowModel

interface ApiHelper {
    suspend fun getTrendingShows(): List<TrendingShowModel>
    suspend fun getDetailByShowId(showId: String): DetailModel
    suspend fun searchShowsByTitle(title: String): List<SearchShowModel>
}
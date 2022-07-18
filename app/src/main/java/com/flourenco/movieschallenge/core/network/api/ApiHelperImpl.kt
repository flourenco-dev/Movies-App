package com.flourenco.movieschallenge.core.network.api

import com.flourenco.movieschallenge.core.network.model.DetailModel
import com.flourenco.movieschallenge.core.network.model.SearchShowModel
import com.flourenco.movieschallenge.core.network.model.TrendingShowModel

internal class ApiHelperImpl(
    private val imdbApi: ImdbApi
): ApiHelper {

    override suspend fun getTrendingShows(): List<TrendingShowModel> =
        imdbApi.getTrendingShows().items ?: emptyList()

    override suspend fun getDetailByShowId(showId: String): DetailModel =
        imdbApi.getDetailByShowId(showId)

    override suspend fun searchShowsByTitle(title: String): List<SearchShowModel> =
        imdbApi.searchShowsByTitle(title).results ?: emptyList()
}
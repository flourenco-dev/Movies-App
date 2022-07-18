package com.flourenco.movieschallenge.core.network.api

import com.flourenco.movieschallenge.core.network.model.DetailModel
import com.flourenco.movieschallenge.core.network.model.SearchShowsResponseModel
import com.flourenco.movieschallenge.core.network.model.TrendingShowsResponseModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ImdbApi {
    @GET("InTheaters/API_KEY")
    suspend fun getTrendingShows(): TrendingShowsResponseModel

    @GET("Title/API_KEY/{showId}")
    suspend fun getDetailByShowId(@Path(value = "showId") showId: String): DetailModel

    @GET("AdvancedSearch/API_KEY/")
    suspend fun searchShowsByTitle(@Query(value = "title") title: String): SearchShowsResponseModel
}
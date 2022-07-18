package com.flourenco.movieschallenge.model

sealed class GetTrendingShowsResult {
    object Failed: GetTrendingShowsResult()
    data class Success(val shows: List<Show>): GetTrendingShowsResult()
}

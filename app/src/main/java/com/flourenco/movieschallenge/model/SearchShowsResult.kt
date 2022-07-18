package com.flourenco.movieschallenge.model

sealed class SearchShowsResult {
    object Failed: SearchShowsResult()
    object MinCharsNotReached: SearchShowsResult()
    data class Success(val shows: List<Show>): SearchShowsResult()
}

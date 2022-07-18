package com.flourenco.movieschallenge.model

sealed class GetDetailByShowIdResult {
    object Failed: GetDetailByShowIdResult()
    data class Success(val detail: Detail): GetDetailByShowIdResult()
}

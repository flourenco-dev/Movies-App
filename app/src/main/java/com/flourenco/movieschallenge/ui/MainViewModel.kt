package com.flourenco.movieschallenge.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flourenco.movieschallenge.core.Repository
import com.flourenco.movieschallenge.core.connectivity.NetworkManager
import com.flourenco.movieschallenge.model.Detail
import com.flourenco.movieschallenge.model.GetDetailByShowIdResult
import com.flourenco.movieschallenge.model.GetTrendingShowsResult
import com.flourenco.movieschallenge.model.SearchShowsResult
import com.flourenco.movieschallenge.model.Show
import com.flourenco.movieschallenge.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val repository: Repository,
    private val networkManager: NetworkManager
): ViewModel() {

    companion object {
        const val minCharsToSearch = 3
    }

    private val showLoadingEvent = SingleLiveEvent<Any>()
    val showLoadingObservable: LiveData<Any> = showLoadingEvent

    private val hideLoadingEvent = SingleLiveEvent<Any>()
    val hideLoadingObservable: LiveData<Any> = hideLoadingEvent

    private val trendingShowsResultLiveData = MutableLiveData<GetTrendingShowsResult>()
    val trendingShowsResultObservable
            : LiveData<GetTrendingShowsResult> = trendingShowsResultLiveData

    private val searchShowsResultLiveData = MutableLiveData<SearchShowsResult>()
    val searchShowsResultObservable: LiveData<SearchShowsResult> = searchShowsResultLiveData

    private val detailByShowIdResultLiveData = MutableLiveData<GetDetailByShowIdResult>()
    val detailByShowIdResultObservable
            : LiveData<GetDetailByShowIdResult> = detailByShowIdResultLiveData

    private val networkConnectedStatusLiveData = MutableLiveData(true)
    val networkConnectedStatusObservable: LiveData<Boolean> = networkConnectedStatusLiveData

    init {
        networkManager.startObservingNetworkChanges()
        triggerCheckConnection()
    }

    fun triggerCheckConnection() {
        viewModelScope.launch {
            val isConnected = networkManager.triggerCheckConnection()
            networkConnectedStatusLiveData.postValue(isConnected)
        }
    }

    fun getNetworkStateObservable() = networkManager.getNetworkStateObservable()

    override fun onCleared() {
        super.onCleared()
        networkManager.stopObservingNetworkChanges()
    }

    fun getFavoritesObservable(): LiveData<List<Show>> = repository.getFavoritesObservable()

    fun triggerGetTrendingShows() {
        viewModelScope.launch {
            try {
                showLoadingEvent.call()
                val trendingShows = repository.getTrendingShows()
                trendingShowsResultLiveData.postValue(
                    GetTrendingShowsResult.Success(
                        shows = trendingShows
                    )
                )
            } catch (error: Exception) {
                Timber.e(error)
                trendingShowsResultLiveData.postValue(GetTrendingShowsResult.Failed)
            } finally {
                hideLoadingEvent.call()
            }
        }
    }

    fun triggerSearchShowsByName(name: String) {
        if (name.length < minCharsToSearch) {
            searchShowsResultLiveData.postValue(SearchShowsResult.MinCharsNotReached)
        } else {
            viewModelScope.launch {
                try {
                    val shows = repository.searchShowsByName(name)
                    searchShowsResultLiveData.postValue(
                        SearchShowsResult.Success(
                            shows = shows
                        )
                    )
                } catch (error: Exception) {
                    Timber.e(error)
                    searchShowsResultLiveData.postValue(SearchShowsResult.Failed)
                }
            }
        }
    }

    fun triggerGetDetailByShowId(showId: String) {
        viewModelScope.launch {
            try {
                showLoadingEvent.call()
                val detail = repository.getDetailByShowId(showId)
                detailByShowIdResultLiveData.postValue(
                    GetDetailByShowIdResult.Success(
                        detail = detail
                    )
                )
            } catch (error: Exception) {
                Timber.e(error)
                detailByShowIdResultLiveData.postValue(GetDetailByShowIdResult.Failed)
            } finally {
                hideLoadingEvent.call()
            }
        }
    }

    fun changeShowIsFavoriteStatus(detail: Detail?) {
        detail?.let {
            viewModelScope.launch {
                try {
                    if (it.isFavorite) {
                        repository.addFavorite(it)
                    } else {
                        repository.deleteFavorite(it)
                    }
                } catch (error: Exception) {
                    Timber.e(error)
                }
            }
        }
    }

    fun changeShowIsHiddenStatus(detail: Detail?) {
        detail?.let {
            if (it.isHidden) {
                repository.addToHiddenShows(it.id)
            } else {
                repository.removeFromHiddenShows(it.id)
            }
        }
    }

    fun clearHiddenShows() {
        repository.removeAllHiddenShows()
    }
}
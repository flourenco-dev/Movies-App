package com.flourenco.movieschallenge.core.connectivity

import androidx.lifecycle.LiveData

interface NetworkManager {
    fun getNetworkStateObservable(): LiveData<Boolean>
    fun startObservingNetworkChanges()
    fun stopObservingNetworkChanges()
    suspend fun triggerCheckConnection(): Boolean
}
package com.flourenco.movieschallenge.core.connectivity

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.net.InetAddress
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

internal class NetworkManagerImpl(private val connectivityManager: ConnectivityManager) :
    NetworkManager, ConnectivityManager.NetworkCallback() {

    private val networkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()
    private val networkStateObservable = MutableLiveData(true)

    override fun getNetworkStateObservable(): LiveData<Boolean> = networkStateObservable

    override fun startObservingNetworkChanges() {
        connectivityManager.registerNetworkCallback(networkRequest, this)
    }

    override fun stopObservingNetworkChanges() {
        try {
            connectivityManager.unregisterNetworkCallback(this)
        } catch (exception: Exception) {
            Timber.d("Network callback already unregistered")
        }
    }

    override suspend fun triggerCheckConnection(): Boolean = checkIfIsReallyConnectedAsync().await()

    private suspend fun checkIfIsReallyConnectedAsync(): Deferred<Boolean> {
        val deferred = CompletableDeferred<Boolean>()
        withContext(Dispatchers.IO) {
            val isConnected = try {
                val ipAddress: InetAddress = InetAddress.getByName("google.com")
                !ipAddress.equals("")
            } catch (e: Exception) {
                Timber.w(e)
                false
            }
            deferred.complete(isConnected)
        }
        return deferred
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkStateObservable.postValue(false)
    }

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkStateObservable.postValue(true)
    }
}
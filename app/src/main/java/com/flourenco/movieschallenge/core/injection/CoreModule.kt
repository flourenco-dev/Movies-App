package com.flourenco.movieschallenge.core.injection

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.flourenco.movieschallenge.R
import com.flourenco.movieschallenge.core.Repository
import com.flourenco.movieschallenge.core.RepositoryImpl
import com.flourenco.movieschallenge.core.connectivity.NetworkManager
import com.flourenco.movieschallenge.core.connectivity.NetworkManagerImpl
import com.flourenco.movieschallenge.core.network.api.ApiHelper
import com.flourenco.movieschallenge.core.network.api.ApiHelperImpl
import com.flourenco.movieschallenge.core.network.api.ApiInterceptor
import com.flourenco.movieschallenge.core.network.api.ImdbApi
import com.flourenco.movieschallenge.core.network.utils.LoggerHttpClient
import com.flourenco.movieschallenge.core.storage.FavoritesDatabase
import com.flourenco.movieschallenge.core.storage.StorageHelper
import com.flourenco.movieschallenge.core.storage.StorageHelperImpl
import com.flourenco.movieschallenge.utils.encryptedSharedPreferencesFileName
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val coreModule = module {
    single<Repository> {
        RepositoryImpl(
            storageHelper = get(),
            apiHelper = get()
        )
    }
}

val networkModule = module {
    single {
        ApiInterceptor(
            apiKey = androidContext().getString(R.string.api_key)
        )
    }
    single<ImdbApi> {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.base_url))
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                LoggerHttpClient
                    .getOkHttpClient()
                    .addInterceptor(get<ApiInterceptor>())
                    .build()
            )
            .build()
            .create(ImdbApi::class.java)
    }
    single<ApiHelper> {
        ApiHelperImpl(imdbApi = get())
    }
}

val storageModule = module {
    single {
        EncryptedSharedPreferences
            .create(
                androidContext(),
                encryptedSharedPreferencesFileName,
                MasterKey.Builder(
                    androidContext(),
                    MasterKey.DEFAULT_MASTER_KEY_ALIAS
                )
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
    }
    single {
        Room.databaseBuilder(
            androidApplication(),
            FavoritesDatabase::class.java,
            "FavoritesDatabase.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single<StorageHelper> {
        StorageHelperImpl(
            preferences = get(),
            favoritesDao = get<FavoritesDatabase>().favoritesDao
        )
    }
    single {
        androidContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    single<NetworkManager> {
        NetworkManagerImpl(get())
    }
}
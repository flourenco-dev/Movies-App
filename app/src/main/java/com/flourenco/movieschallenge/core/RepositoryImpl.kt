package com.flourenco.movieschallenge.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.flourenco.movieschallenge.core.network.api.ApiHelper
import com.flourenco.movieschallenge.core.network.model.DetailModel
import com.flourenco.movieschallenge.core.storage.StorageHelper
import com.flourenco.movieschallenge.core.storage.entity.FavoriteEntity
import com.flourenco.movieschallenge.model.Detail
import com.flourenco.movieschallenge.model.DetailInfo
import com.flourenco.movieschallenge.model.DetailInfoType
import com.flourenco.movieschallenge.model.Show
import com.flourenco.movieschallenge.model.exception.DatabaseStorageException
import com.flourenco.movieschallenge.utils.get

internal class RepositoryImpl(
    private val storageHelper: StorageHelper,
    private val apiHelper: ApiHelper
    ): Repository {

    override fun getFavoritesObservable(): LiveData<List<Show>> = storageHelper.getFavorites().map {
        it.toShows()
    }

    override suspend fun addFavorite(detail: Detail) {
        val addResult = storageHelper.addFavorite(detail.toFavoriteEntity())
        if (!addResult) {
            throw DatabaseStorageException()
        }
    }

    private fun Detail.toFavoriteEntity(): FavoriteEntity {
        var favoriteEntity = FavoriteEntity(
            id = id,
            name = name,
            imagerUrl = imagerUrl
        )
        detailInfos.forEach {
            when (it.header) {
                DetailInfoType.Duration -> {
                    favoriteEntity = favoriteEntity.copy(
                        duration = it.content
                    )
                }
                DetailInfoType.Description -> {
                    favoriteEntity = favoriteEntity.copy(
                        description = it.content
                    )
                }
                DetailInfoType.Rating -> {
                    favoriteEntity = favoriteEntity.copy(
                        rating = it.content
                    )
                }
                DetailInfoType.Genres -> {
                    favoriteEntity = favoriteEntity.copy(
                        genres = it.content
                    )
                }
                DetailInfoType.Stars -> {
                    favoriteEntity = favoriteEntity.copy(
                        stars = it.content
                    )
                }
                else -> {}
            }
        }
        return favoriteEntity
    }

    override suspend fun deleteFavorite(detail: Detail) {
        val deleteResult = storageHelper.deleteFavorite(detail.toFavoriteEntity())
        if (!deleteResult) {
            throw DatabaseStorageException()
        }
    }

    override fun addToHiddenShows(showId: String) {
        storageHelper.addHiddenShowId(showId)
    }

    override fun removeFromHiddenShows(showId: String) {
        storageHelper.removeHiddenShowId(showId)
    }

    override fun removeAllHiddenShows() {
        storageHelper.removeAllHiddenShowIds()
    }

    private fun List<FavoriteEntity>.toShows(): List<Show> = map {
        it.toShow()
    }

    private fun FavoriteEntity.toShow(): Show = Show(
        id = id,
        name = name,
        imagerUrl = imagerUrl,
        description = description,
        rating = rating,
        duration = duration,
        genres = genres,
        stars = stars
    )

    override suspend fun getDetailByShowId(showId: String): Detail {
        val isFavorite = storageHelper.isFavorite(favoriteId = showId)
        val isHidden = storageHelper.getHiddenShowIds().contains(showId)
        return apiHelper.getDetailByShowId(showId).toDetail(
            isFavorite = isFavorite,
            isHidden = isHidden
        )
    }

    private fun DetailModel.toDetail(
        isFavorite: Boolean,
        isHidden: Boolean
    ): Detail = Detail(
        id = id.get(),
        name = fullTitle.get(),
        imagerUrl = image,
        detailInfos = mutableListOf<DetailInfo>().also { detailInfos ->
            runtimeStr?.let {
                detailInfos.add(
                    DetailInfo(
                        header = DetailInfoType.Duration,
                        content = it
                    )
                )
            }
            plot?.let {
                detailInfos.add(
                    DetailInfo(
                        header = DetailInfoType.Description,
                        content = it
                    )
                )
            }
            imDbRating?.let {
                detailInfos.add(
                    DetailInfo(
                        header = DetailInfoType.Rating,
                        content = it
                    )
                )
            }
            genres?.let {
                detailInfos.add(
                    DetailInfo(
                        header = DetailInfoType.Genres,
                        content = it
                    )
                )
            }
            type?.let {
                detailInfos.add(
                    DetailInfo(
                        header = DetailInfoType.Type,
                        content = it
                    )
                )
            }
            directors?.let {
                detailInfos.add(
                    DetailInfo(
                        header = DetailInfoType.Directors,
                        content = it
                    )
                )
            }
            writers?.let {
                detailInfos.add(
                    DetailInfo(
                        header = DetailInfoType.Writers,
                        content = it
                    )
                )
            }
            companies?.let {
                detailInfos.add(
                    DetailInfo(
                        header = DetailInfoType.Companies,
                        content = it
                    )
                )
            }
            countries?.let {
                detailInfos.add(
                    DetailInfo(
                        header = DetailInfoType.Countries,
                        content = it
                    )
                )
            }
            stars?.let {
                detailInfos.add(
                    DetailInfo(
                        header = DetailInfoType.Stars,
                        content = it
                    )
                )
            }
        },
        isFavorite = isFavorite,
        isHidden = isHidden
    )

    override suspend fun searchShowsByName(name: String): List<Show> =
        apiHelper.searchShowsByTitle(title = name).filter {
            it.id.isNullOrEmpty().not() && it.title.isNullOrEmpty().not()
        }.filterNot {
            storageHelper.getHiddenShowIds().contains(it.id)
        }.map {
            Show(
                id = it.id.get(),
                name = "${it.title} ${it.description.get()}",
                imagerUrl = it.image,
                description = it.plot,
                rating = it.imDbRating,
                duration = it.runtimeStr,
                genres = it.genres,
                stars = it.stars
            )
        }

    override suspend fun getTrendingShows(): List<Show> = apiHelper.getTrendingShows().filter {
        it.id.isNullOrEmpty().not() && it.fullTitle.isNullOrEmpty().not()
    }.filterNot {
        storageHelper.getHiddenShowIds().contains(it.id)
    }.map {
        Show(
            id = it.id.get(),
            name = it.fullTitle.get(),
            imagerUrl = it.image,
            description = it.plot,
            rating = it.imDbRating,
            duration = it.runtimeStr,
            genres = it.genres,
            stars = it.stars
        )
    }
}
package com.yumedev.soraspace.domain.repository

import com.yumedev.soraspace.domain.model.Apod
import com.yumedev.soraspace.domain.model.NasaMedia
import com.yumedev.soraspace.domain.model.SpaceArticle
import com.yumedev.soraspace.domain.model.SpaceLaunch
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeApodFavorites(): Flow<List<Apod>>
    fun observeMediaFavorites(): Flow<List<NasaMedia>>
    fun observeLaunchFavorites(): Flow<List<SpaceLaunch>>
    fun observeArticleFavorites(): Flow<List<SpaceArticle>>

    suspend fun toggleApodFavorite(apod: Apod)
    suspend fun toggleMediaFavorite(media: NasaMedia)
    suspend fun toggleLaunchFavorite(launch: SpaceLaunch)
    suspend fun toggleArticleFavorite(article: SpaceArticle)
}
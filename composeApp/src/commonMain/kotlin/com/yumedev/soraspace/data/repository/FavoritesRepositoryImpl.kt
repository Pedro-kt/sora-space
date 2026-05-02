package com.yumedev.soraspace.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.yumedev.soraspace.database.SoraDatabase
import com.yumedev.soraspace.domain.model.Apod
import com.yumedev.soraspace.domain.model.NasaMedia
import com.yumedev.soraspace.domain.model.SpaceArticle
import com.yumedev.soraspace.domain.model.SpaceLaunch
import com.yumedev.soraspace.domain.repository.FavoritesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class FavoritesRepositoryImpl(private val db: SoraDatabase) : FavoritesRepository {

    override fun observeApodFavorites(): Flow<List<Apod>> =
        db.apodFavoriteQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows ->
                rows.map { row ->
                    Apod(
                        title       = row.title,
                        explanation = row.explanation,
                        imageUrl    = row.image_url,
                        hdImageUrl  = row.hd_image_url,
                        date        = row.date,
                        mediaType   = row.media_type,
                        copyright   = row.copyright
                    )
                }
            }

    override fun observeMediaFavorites(): Flow<List<NasaMedia>> =
        db.mediaFavoriteQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows ->
                rows.map { row ->
                    NasaMedia(
                        id           = row.id,
                        title        = row.title,
                        description  = row.description,
                        thumbnailUrl = row.thumbnail_url,
                        mediaType    = row.media_type,
                        dateCreated  = row.date_created,
                        center       = row.center
                    )
                }
            }

    override fun observeLaunchFavorites(): Flow<List<SpaceLaunch>> =
        db.launchFavoriteQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows ->
                rows.map { row ->
                    SpaceLaunch(
                        id                 = row.id,
                        name               = row.name,
                        net                = row.net,
                        url                = row.url,
                        windowStart        = row.window_start,
                        windowEnd          = row.window_end,
                        statusName         = row.status_name,
                        statusAbbrev       = row.status_abbrev,
                        provider           = row.provider,
                        missionName        = row.mission_name,
                        missionDescription = row.mission_description,
                        missionType        = row.mission_type,
                        orbit              = row.orbit,
                        padName            = row.pad_name,
                        location           = row.location,
                        imageUrl           = row.image_url
                    )
                }
            }

    override fun observeArticleFavorites(): Flow<List<SpaceArticle>> =
        db.articleFavoriteQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { rows ->
                rows.map { row ->
                    SpaceArticle(
                        id          = row.id.toInt(),
                        title       = row.title,
                        url         = row.url,
                        imageUrl    = row.image_url,
                        newsSite    = row.news_site,
                        summary     = row.summary,
                        publishedAt = row.published_at,
                        updatedAt   = row.updated_at,
                        authors     = if (row.authors.isEmpty()) emptyList() else row.authors.split("|")
                    )
                }
            }

    override suspend fun toggleApodFavorite(apod: Apod) = withContext(Dispatchers.Default) {
        val exists = db.apodFavoriteQueries.selectByDate(apod.date).executeAsOneOrNull()
        if (exists != null) {
            db.apodFavoriteQueries.delete(apod.date)
        } else {
            db.apodFavoriteQueries.insertOrReplace(
                date         = apod.date,
                title        = apod.title,
                explanation  = apod.explanation,
                image_url    = apod.imageUrl,
                hd_image_url = apod.hdImageUrl,
                media_type   = apod.mediaType,
                copyright    = apod.copyright
            )
        }
    }

    override suspend fun toggleMediaFavorite(media: NasaMedia) = withContext(Dispatchers.Default) {
        val exists = db.mediaFavoriteQueries.selectById(media.id).executeAsOneOrNull()
        if (exists != null) {
            db.mediaFavoriteQueries.delete(media.id)
        } else {
            db.mediaFavoriteQueries.insertOrReplace(
                id            = media.id,
                title         = media.title,
                description   = media.description,
                thumbnail_url = media.thumbnailUrl,
                media_type    = media.mediaType,
                date_created  = media.dateCreated,
                center        = media.center
            )
        }
    }

    override suspend fun toggleLaunchFavorite(launch: SpaceLaunch) = withContext(Dispatchers.Default) {
        val exists = db.launchFavoriteQueries.selectById(launch.id).executeAsOneOrNull()
        if (exists != null) {
            db.launchFavoriteQueries.delete(launch.id)
        } else {
            db.launchFavoriteQueries.insertOrReplace(
                id                  = launch.id,
                name                = launch.name,
                net                 = launch.net,
                url                 = launch.url,
                window_start        = launch.windowStart,
                window_end          = launch.windowEnd,
                status_name         = launch.statusName,
                status_abbrev       = launch.statusAbbrev,
                provider            = launch.provider,
                mission_name        = launch.missionName,
                mission_description = launch.missionDescription,
                mission_type        = launch.missionType,
                orbit               = launch.orbit,
                pad_name            = launch.padName,
                location            = launch.location,
                image_url           = launch.imageUrl
            )
        }
    }

    override suspend fun toggleArticleFavorite(article: SpaceArticle) = withContext(Dispatchers.Default) {
        val exists = db.articleFavoriteQueries.selectById(article.id.toLong()).executeAsOneOrNull()
        if (exists != null) {
            db.articleFavoriteQueries.delete(article.id.toLong())
        } else {
            db.articleFavoriteQueries.insertOrReplace(
                id           = article.id.toLong(),
                title        = article.title,
                url          = article.url,
                image_url    = article.imageUrl,
                news_site    = article.newsSite,
                summary      = article.summary,
                published_at = article.publishedAt,
                updated_at   = article.updatedAt,
                authors      = article.authors.joinToString("|")
            )
        }
    }
}
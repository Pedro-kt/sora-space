package com.yumedev.soraspace.data.repository

import com.yumedev.soraspace.data.remote.NasaApiService
import com.yumedev.soraspace.data.remote.dto.NasaMediaItem
import com.yumedev.soraspace.domain.model.NasaMedia
import com.yumedev.soraspace.domain.repository.MediaRepository

class MediaRepositoryImpl(
    private val apiService: NasaApiService = NasaApiService()
) : MediaRepository {

    override suspend fun search(query: String): List<NasaMedia> =
        apiService.searchMedia(query).collection.items.mapNotNull { it.toDomain() }

    private fun NasaMediaItem.toDomain(): NasaMedia? {
        val data = data.firstOrNull() ?: return null
        val thumbnail = links?.firstOrNull { it.rel == "preview" }?.href
        return NasaMedia(
            id           = data.nasaId,
            title        = data.title,
            description  = data.description,
            thumbnailUrl = thumbnail,
            mediaType    = data.mediaType,
            dateCreated  = data.dateCreated?.take(10),
            center       = data.center
        )
    }
}
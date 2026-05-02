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

    override suspend fun getAssetUrl(nasaId: String, isVideo: Boolean): String? {
        val items = apiService.getMediaAsset(nasaId).collection.items.map { it.href }
        val raw = if (isVideo) {
            items.firstOrNull { it.endsWith("~mobile.mp4") }
                ?: items.firstOrNull { it.endsWith("~medium.mp4") }
                ?: items.firstOrNull { it.endsWith("~small.mp4") }
                ?: items.firstOrNull { it.endsWith(".mp4") }
        } else {
            items.firstOrNull { it.endsWith("~orig.jpg") }
                ?: items.firstOrNull { it.endsWith("~large.jpg") }
                ?: items.firstOrNull { it.endsWith("~medium.jpg") }
                ?: items.firstOrNull { it.endsWith(".jpg") }
        }
        return raw?.replace("http://", "https://")?.replace(" ", "%20")
    }

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
package com.yumedev.soraspace.data.remote

import com.yumedev.soraspace.data.remote.dto.ApodDto
import com.yumedev.soraspace.data.remote.dto.AsteroidFeedDto
import com.yumedev.soraspace.data.remote.dto.DonkiFlrDto
import com.yumedev.soraspace.data.remote.dto.EonetResponseDto
import com.yumedev.soraspace.data.remote.dto.NasaAssetResponse
import com.yumedev.soraspace.data.remote.dto.NasaMediaCollectionResponse
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class NasaApiService {

    private val client = createHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getApodOfTheDay(): ApodDto =
        json.decodeFromString(fetch("$BASE_URL/planetary/apod") {
            parameter("api_key", API_KEY)
        })

    suspend fun getApods(startDate: String, endDate: String): List<ApodDto> =
        json.decodeFromString(fetch("$BASE_URL/planetary/apod") {
            parameter("api_key", API_KEY)
            parameter("start_date", startDate)
            parameter("end_date", endDate)
        })

    suspend fun getAsteroidFeed(startDate: String, endDate: String): AsteroidFeedDto =
        json.decodeFromString(fetch("$BASE_URL/neo/rest/v1/feed") {
            parameter("api_key", API_KEY)
            parameter("start_date", startDate)
            parameter("end_date", endDate)
        })

    suspend fun getMediaAsset(nasaId: String): NasaAssetResponse =
        json.decodeFromString(fetch("$MEDIA_BASE_URL/asset/$nasaId"))

    suspend fun searchMedia(query: String): NasaMediaCollectionResponse =
        json.decodeFromString(fetch("$MEDIA_BASE_URL/search") {
            parameter("q", query)
            parameter("media_type", "image,video")
        })

    suspend fun getRecentFlares(startDate: String, endDate: String): List<DonkiFlrDto> =
        json.decodeFromString(fetch("$BASE_URL/DONKI/FLR") {
            parameter("api_key", API_KEY)
            parameter("startDate", startDate)
            parameter("endDate", endDate)
        })

    suspend fun getEonetEvents(): EonetResponseDto =
        json.decodeFromString(fetch("$EONET_BASE_URL/events") {
            parameter("status", "open")
            parameter("limit", "100")
        })

    private suspend fun fetch(
        url: String,
        block: io.ktor.client.request.HttpRequestBuilder.() -> Unit = {}
    ): String {
        val response = client.get(url, block)
        val raw = response.bodyAsText()

        val errorMessage = runCatching {
            json.parseToJsonElement(raw)
                .jsonObject["error"]
                ?.jsonObject?.get("message")?.jsonPrimitive?.content
        }.getOrNull()

        if (errorMessage != null) throw NasaApiException(errorMessage)
        if (!response.status.isSuccess()) {
            throw NasaApiException("HTTP ${response.status.value}: ${response.status.description}")
        }

        return raw
    }

    companion object {
        private const val BASE_URL       = "https://api.nasa.gov"
        private const val MEDIA_BASE_URL = "https://images-api.nasa.gov"
        private const val EONET_BASE_URL = "https://eonet.gsfc.nasa.gov/api/v3"
        private val API_KEY = com.yumedev.soraspace.BuildConfig.NASA_API_KEY
    }
}

class NasaApiException(message: String) : Exception(message)
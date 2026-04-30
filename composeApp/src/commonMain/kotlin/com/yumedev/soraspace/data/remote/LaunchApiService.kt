package com.yumedev.soraspace.data.remote

import com.yumedev.soraspace.data.remote.dto.LaunchDto
import com.yumedev.soraspace.data.remote.dto.LaunchListDto
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class LaunchApiService {

    private val client = createHttpClient()
    private val json = Json { ignoreUnknownKeys = true; isLenient = true }

    suspend fun getUpcomingLaunches(limit: Int = 6): List<LaunchDto> {
        val raw = client.get("$BASE_URL/launches/upcoming/") {
            parameter("limit", limit)
            parameter("format", "json")
        }.bodyAsText()
        return json.decodeFromString<LaunchListDto>(raw).results
    }

    companion object {
        private const val BASE_URL = "https://ll.thespacedevs.com/2.3.0"
    }
}
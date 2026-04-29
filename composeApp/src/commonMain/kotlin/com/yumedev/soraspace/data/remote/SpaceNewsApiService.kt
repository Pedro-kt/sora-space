package com.yumedev.soraspace.data.remote

import com.yumedev.soraspace.data.remote.dto.ArticleDto
import com.yumedev.soraspace.data.remote.dto.PaginatedArticleListDto
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class SpaceNewsApiService {

    private val client = createHttpClient()
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun getArticles(limit: Int = 10): List<ArticleDto> {
        val raw = client.get("$BASE_URL/articles/") {
            parameter("limit", limit)
            parameter("ordering", "-published_at")
        }.bodyAsText()
        return json.decodeFromString<PaginatedArticleListDto>(raw).results
    }

    companion object {
        private const val BASE_URL = "https://api.spaceflightnewsapi.net/v4"
    }
}
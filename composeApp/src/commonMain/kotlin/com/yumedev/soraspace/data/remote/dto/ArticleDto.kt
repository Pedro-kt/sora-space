package com.yumedev.soraspace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedArticleListDto(
    val count: Int,
    val results: List<ArticleDto>
)

@Serializable
data class AuthorDto(
    val name: String
)

@Serializable
data class ArticleDto(
    val id: Int,
    val title: String,
    val url: String,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("news_site") val newsSite: String,
    val summary: String,
    @SerialName("published_at") val publishedAt: String,
    @SerialName("updated_at") val updatedAt: String = "",
    val authors: List<AuthorDto> = emptyList(),
    val featured: Boolean = false
)
package com.yumedev.soraspace.data.repository

import com.yumedev.soraspace.data.remote.SpaceNewsApiService
import com.yumedev.soraspace.data.remote.dto.ArticleDto
import com.yumedev.soraspace.domain.model.SpaceArticle
import com.yumedev.soraspace.domain.repository.SpaceNewsRepository

class SpaceNewsRepositoryImpl : SpaceNewsRepository {

    private val api = SpaceNewsApiService()

    override suspend fun getLatestArticles(limit: Int): List<SpaceArticle> =
        api.getArticles(limit).map { it.toDomain() }
}

private fun ArticleDto.toDomain() = SpaceArticle(
    id          = id,
    title       = title,
    url         = url,
    imageUrl    = imageUrl,
    newsSite    = newsSite,
    summary     = summary,
    publishedAt = publishedAt,
    updatedAt   = updatedAt,
    authors     = authors.map { it.name }
)
package com.yumedev.soraspace.domain.repository

import com.yumedev.soraspace.domain.model.SpaceArticle

interface SpaceNewsRepository {
    suspend fun getLatestArticles(limit: Int = 10): List<SpaceArticle>
}
package com.yumedev.soraspace.domain.model

data class SpaceArticle(
    val id: Int,
    val title: String,
    val url: String,
    val imageUrl: String,
    val newsSite: String,
    val summary: String,
    val publishedAt: String,
    val updatedAt: String,
    val authors: List<String>
)
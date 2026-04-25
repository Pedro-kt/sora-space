package com.yumedev.soraspace.domain.model

data class Apod(
    val title: String,
    val explanation: String,
    val imageUrl: String,
    val hdImageUrl: String?,
    val date: String,
    val mediaType: String,
    val copyright: String?
)
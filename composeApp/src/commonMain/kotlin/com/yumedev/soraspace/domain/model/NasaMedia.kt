package com.yumedev.soraspace.domain.model

data class NasaMedia(
    val id: String,
    val title: String,
    val description: String?,
    val thumbnailUrl: String?,
    val mediaType: String,
    val dateCreated: String?,
    val center: String?
) {
    val isVideo get() = mediaType == "video"
}

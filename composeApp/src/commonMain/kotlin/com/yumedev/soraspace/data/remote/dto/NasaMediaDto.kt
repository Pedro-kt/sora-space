package com.yumedev.soraspace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NasaMediaCollectionResponse(
    val collection: NasaMediaCollection
)

@Serializable
data class NasaMediaCollection(
    val items: List<NasaMediaItem> = emptyList(),
    val metadata: NasaMediaMetadata? = null
)

@Serializable
data class NasaMediaItem(
    val href: String,
    val data: List<NasaMediaData> = emptyList(),
    val links: List<NasaMediaLink>? = null
)

@Serializable
data class NasaMediaData(
    val title: String = "",
    val description: String? = null,
    @SerialName("date_created") val dateCreated: String? = null,
    @SerialName("media_type") val mediaType: String = "image",
    @SerialName("nasa_id") val nasaId: String = "",
    val center: String? = null
)

@Serializable
data class NasaMediaLink(
    val href: String,
    val rel: String,
    val render: String? = null
)

@Serializable
data class NasaMediaMetadata(
    @SerialName("total_hits") val totalHits: Int = 0
)

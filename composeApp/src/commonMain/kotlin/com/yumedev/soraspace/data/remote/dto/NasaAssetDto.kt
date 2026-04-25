package com.yumedev.soraspace.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class NasaAssetResponse(
    val collection: NasaAssetCollection
)

@Serializable
data class NasaAssetCollection(
    val items: List<NasaAssetItem> = emptyList()
)

@Serializable
data class NasaAssetItem(
    val href: String
)
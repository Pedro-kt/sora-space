package com.yumedev.soraspace.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EonetResponseDto(
    val events: List<EonetEventDto>
)

@Serializable
data class EonetEventDto(
    val id: String,
    val title: String,
    val closed: String? = null,
    val categories: List<EonetCategoryDto> = emptyList(),
    val geometry: List<EonetGeometryDto> = emptyList()
)

@Serializable
data class EonetCategoryDto(
    val id: String,
    val title: String
)

@Serializable
data class EonetGeometryDto(
    val date: String,
    val type: String
)
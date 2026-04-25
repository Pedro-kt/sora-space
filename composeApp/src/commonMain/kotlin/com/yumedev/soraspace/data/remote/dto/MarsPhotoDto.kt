package com.yumedev.soraspace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MarsPhotosResponse(
    val photos: List<MarsPhotoDto>
)

@Serializable
data class MarsPhotoDto(
    val id: Int,
    val sol: Int,
    val camera: MarsCameraDto,
    @SerialName("img_src") val imgSrc: String,
    @SerialName("earth_date") val earthDate: String,
    val rover: MarsRoverDto
)

@Serializable
data class MarsCameraDto(
    val name: String,
    @SerialName("full_name") val fullName: String
)

@Serializable
data class MarsRoverDto(
    val name: String,
    val status: String
)
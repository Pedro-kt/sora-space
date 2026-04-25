package com.yumedev.soraspace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApodDto(
    val title: String,
    val explanation: String,
    val url: String,
    @SerialName("hdurl") val hdUrl: String? = null,
    val date: String,
    @SerialName("media_type") val mediaType: String,
    val copyright: String? = null
)
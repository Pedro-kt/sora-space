package com.yumedev.soraspace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LaunchListDto(
    val count: Int = 0,
    val results: List<LaunchDto> = emptyList()
)

@Serializable
data class LaunchDto(
    val id: String,
    val name: String,
    val net: String = "",
    val status: LaunchStatusDto = LaunchStatusDto(),
    @SerialName("launch_service_provider") val provider: LaunchProviderDto? = null,
    val mission: LaunchMissionDto? = null,
    val pad: LaunchPadDto? = null,
    val image: LaunchImageDto? = null
)

@Serializable
data class LaunchStatusDto(
    val id: Int = 0,
    val name: String = "TBD",
    val abbrev: String = "TBD"
)

@Serializable
data class LaunchProviderDto(
    val id: Int = 0,
    val name: String = ""
)

@Serializable
data class LaunchMissionDto(
    val id: Int = 0,
    val name: String = "",
    val type: String = ""
)

@Serializable
data class LaunchPadDto(
    val id: Int = 0,
    val name: String = "",
    val location: LaunchLocationDto? = null
)

@Serializable
data class LaunchLocationDto(
    val id: Int = 0,
    val name: String = ""
)

@Serializable
data class LaunchImageDto(
    @SerialName("image_url") val imageUrl: String? = null
)
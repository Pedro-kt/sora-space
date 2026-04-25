package com.yumedev.soraspace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AsteroidFeedDto(
    @SerialName("element_count") val elementCount: Int,
    @SerialName("near_earth_objects") val nearEarthObjects: Map<String, List<AsteroidDto>>
)

@Serializable
data class AsteroidDto(
    val id: String,
    val name: String,
    @SerialName("absolute_magnitude_h") val absoluteMagnitude: Double,
    @SerialName("estimated_diameter") val estimatedDiameter: EstimatedDiameterContainerDto,
    @SerialName("is_potentially_hazardous_asteroid") val isPotentiallyHazardous: Boolean,
    @SerialName("close_approach_data") val closeApproachData: List<CloseApproachDto>
)

@Serializable
data class EstimatedDiameterContainerDto(
    val kilometers: EstimatedDiameterDto
)

@Serializable
data class EstimatedDiameterDto(
    @SerialName("estimated_diameter_min") val min: Double,
    @SerialName("estimated_diameter_max") val max: Double
)

@Serializable
data class CloseApproachDto(
    @SerialName("close_approach_date") val date: String,
    @SerialName("relative_velocity") val velocity: VelocityDto,
    @SerialName("miss_distance") val missDistance: MissDistanceDto
)

@Serializable
data class VelocityDto(
    @SerialName("kilometers_per_hour") val kmPerHour: String
)

@Serializable
data class MissDistanceDto(
    val kilometers: String,
    val lunar: String
)
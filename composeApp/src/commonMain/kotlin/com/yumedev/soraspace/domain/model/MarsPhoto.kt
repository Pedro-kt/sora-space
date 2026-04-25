package com.yumedev.soraspace.domain.model

data class MarsPhoto(
    val id: Int,
    val imageUrl: String,
    val cameraName: String,
    val cameraFullName: String,
    val sol: Int,
    val earthDate: String,
    val roverName: String
)

enum class Rover(val apiName: String, val displayName: String) {
    CURIOSITY("curiosity", "Curiosity"),
    PERSEVERANCE("perseverance", "Perseverance"),
    OPPORTUNITY("opportunity", "Opportunity"),
    SPIRIT("spirit", "Spirit")
}
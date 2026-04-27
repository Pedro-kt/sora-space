package com.yumedev.soraspace.domain.model

data class SpaceWeather(
    val activityLevel: ActivityLevel,
    val latestFlare: SolarFlare?,
    val flareCount: Int
)

data class SolarFlare(
    val classType: String,
    val date: String
)

enum class ActivityLevel { QUIET, MINOR, MODERATE, SEVERE }
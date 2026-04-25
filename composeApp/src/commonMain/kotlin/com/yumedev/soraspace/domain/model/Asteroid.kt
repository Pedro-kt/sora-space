package com.yumedev.soraspace.domain.model

data class Asteroid(
    val id: String,
    val name: String,
    val isPotentiallyHazardous: Boolean,
    val diameterMinKm: Double,
    val diameterMaxKm: Double,
    val closestApproachDate: String,
    val missDistanceLunar: Double,
    val missDistanceKm: Double,
    val velocityKmH: Double
)
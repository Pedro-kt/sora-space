package com.yumedev.soraspace.data.repository

import com.yumedev.soraspace.data.remote.NasaApiService
import com.yumedev.soraspace.data.remote.dto.AsteroidDto
import com.yumedev.soraspace.domain.model.Asteroid
import com.yumedev.soraspace.domain.repository.AsteroidRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class AsteroidRepositoryImpl(
    private val apiService: NasaApiService = NasaApiService()
) : AsteroidRepository {

    override suspend fun getWeeklyFeed(): List<Asteroid> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val endDate = today.plus(DatePeriod(days = 7))
        return apiService
            .getAsteroidFeed(today.toString(), endDate.toString())
            .nearEarthObjects
            .values
            .flatten()
            .mapNotNull { it.toDomain() }
            .sortedBy { it.closestApproachDate }
    }

    private fun AsteroidDto.toDomain(): Asteroid? {
        val approach = closeApproachData.firstOrNull() ?: return null
        return Asteroid(
            id                    = id,
            name                  = name.removePrefix("(").removeSuffix(")").trim(),
            isPotentiallyHazardous = isPotentiallyHazardous,
            diameterMinKm         = estimatedDiameter.kilometers.min,
            diameterMaxKm         = estimatedDiameter.kilometers.max,
            closestApproachDate   = approach.date,
            missDistanceLunar     = approach.missDistance.lunar.toDoubleOrNull() ?: 0.0,
            missDistanceKm        = approach.missDistance.kilometers.toDoubleOrNull() ?: 0.0,
            velocityKmH           = approach.velocity.kmPerHour.toDoubleOrNull() ?: 0.0
        )
    }
}
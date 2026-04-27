package com.yumedev.soraspace.data.repository

import com.yumedev.soraspace.data.remote.NasaApiService
import com.yumedev.soraspace.data.remote.dto.DonkiFlrDto
import com.yumedev.soraspace.domain.model.ActivityLevel
import com.yumedev.soraspace.domain.model.SolarFlare
import com.yumedev.soraspace.domain.model.SpaceWeather
import com.yumedev.soraspace.domain.repository.SpaceWeatherRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class SpaceWeatherRepositoryImpl(
    private val apiService: NasaApiService = NasaApiService()
) : SpaceWeatherRepository {

    override suspend fun getSpaceWeather(): SpaceWeather {
        val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
        val startDate = today.minus(DatePeriod(days = 7)).toString()
        val endDate = today.toString()

        val flares = apiService.getRecentFlares(startDate, endDate)

        val latest = flares
            .maxByOrNull { it.peakTime ?: "" }
            ?.let { dto ->
                SolarFlare(
                    classType = dto.classType ?: "Unknown",
                    date = dto.peakTime?.take(10) ?: ""
                )
            }

        val maxLevel = flares
            .mapNotNull { it.classType?.firstOrNull() }
            .maxOfOrNull { it.toActivityLevel() }
            ?: ActivityLevel.QUIET

        return SpaceWeather(
            activityLevel = maxLevel,
            latestFlare   = latest,
            flareCount    = flares.size
        )
    }

    private fun Char.toActivityLevel(): ActivityLevel = when (this) {
        'X'  -> ActivityLevel.SEVERE
        'M'  -> ActivityLevel.MODERATE
        'C'  -> ActivityLevel.MINOR
        else -> ActivityLevel.QUIET
    }
}
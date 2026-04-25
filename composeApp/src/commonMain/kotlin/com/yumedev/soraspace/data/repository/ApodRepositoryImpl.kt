package com.yumedev.soraspace.data.repository

import com.yumedev.soraspace.data.remote.NasaApiService
import com.yumedev.soraspace.data.remote.dto.ApodDto
import com.yumedev.soraspace.domain.model.Apod
import com.yumedev.soraspace.domain.repository.ApodRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

class ApodRepositoryImpl(
    private val apiService: NasaApiService = NasaApiService()
) : ApodRepository {

    override suspend fun getApodOfTheDay(): Apod =
        apiService.getApodOfTheDay().toDomain()

    override suspend fun getRecentApods(): List<Apod> {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val endDate   = today.minus(DatePeriod(days = 1))   // ayer (el día de hoy ya es el hero)
        val startDate = today.minus(DatePeriod(days = 11))  // 10 días atrás

        return apiService
            .getApods(startDate = startDate.toString(), endDate = endDate.toString())
            .map { it.toDomain() }
            .sortedByDescending { it.date }
    }

    private fun ApodDto.toDomain() = Apod(
        title      = title,
        explanation = explanation,
        imageUrl   = url,
        hdImageUrl = hdUrl,
        date       = date,
        mediaType  = mediaType,
        copyright  = copyright
    )
}
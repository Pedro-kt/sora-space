package com.yumedev.soraspace.data.repository

import com.yumedev.soraspace.data.remote.NasaApiService
import com.yumedev.soraspace.data.remote.dto.EonetEventDto
import com.yumedev.soraspace.domain.model.EonetEvent
import com.yumedev.soraspace.domain.repository.EonetRepository

class EonetRepositoryImpl(
    private val apiService: NasaApiService = NasaApiService()
) : EonetRepository {

    override suspend fun getOpenEvents(): List<EonetEvent> =
        apiService.getEonetEvents().events.map { it.toDomain() }

    private fun EonetEventDto.toDomain(): EonetEvent {
        val category = categories.firstOrNull()
        return EonetEvent(
            id            = id,
            title         = title,
            categoryId    = category?.id ?: "unknown",
            categoryTitle = category?.title ?: "Unknown",
            isOpen        = closed == null,
            date          = geometry.lastOrNull()?.date?.take(10) ?: ""
        )
    }
}
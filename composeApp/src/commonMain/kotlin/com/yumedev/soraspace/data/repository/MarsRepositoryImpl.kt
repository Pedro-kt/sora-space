package com.yumedev.soraspace.data.repository

import com.yumedev.soraspace.data.remote.NasaApiService
import com.yumedev.soraspace.data.remote.dto.MarsPhotoDto
import com.yumedev.soraspace.domain.model.MarsPhoto
import com.yumedev.soraspace.domain.model.Rover
import com.yumedev.soraspace.domain.repository.MarsRepository

class MarsRepositoryImpl(
    private val apiService: NasaApiService = NasaApiService()
) : MarsRepository {

    override suspend fun getLatestPhotos(rover: String): List<MarsPhoto> {
        val date = lastActiveDateFor(rover)
        return apiService.getMarsPhotos(rover, date).photos.map { it.toDomain() }
    }

    // Fechas con fotos conocidas para cada rover
    private fun lastActiveDateFor(rover: String) = when (rover) {
        Rover.CURIOSITY.apiName     -> "2025-01-01"
        Rover.PERSEVERANCE.apiName  -> "2025-01-01"
        Rover.OPPORTUNITY.apiName   -> "2018-06-10"
        Rover.SPIRIT.apiName        -> "2010-03-21"
        else                        -> "2025-01-01"
    }

    private fun MarsPhotoDto.toDomain() = MarsPhoto(
        id             = id,
        imageUrl       = imgSrc,
        cameraName     = camera.name,
        cameraFullName = camera.fullName,
        sol            = sol,
        earthDate      = earthDate,
        roverName      = rover.name
    )
}
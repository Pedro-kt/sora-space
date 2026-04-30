package com.yumedev.soraspace.data.repository

import com.yumedev.soraspace.data.remote.LaunchApiService
import com.yumedev.soraspace.data.remote.dto.LaunchDto
import com.yumedev.soraspace.domain.model.SpaceLaunch
import com.yumedev.soraspace.domain.repository.LaunchRepository

class LaunchRepositoryImpl : LaunchRepository {

    private val api = LaunchApiService()

    override suspend fun getUpcomingLaunches(limit: Int): List<SpaceLaunch> =
        api.getUpcomingLaunches(limit).map { it.toDomain() }
}

private fun LaunchDto.toDomain() = SpaceLaunch(
    id           = id,
    name         = name,
    net          = net,
    statusName   = status.name,
    statusAbbrev = status.abbrev,
    provider     = provider?.name.orEmpty(),
    missionType  = mission?.type.orEmpty(),
    padName      = pad?.name.orEmpty(),
    location     = pad?.location?.name.orEmpty(),
    imageUrl     = image?.imageUrl
)
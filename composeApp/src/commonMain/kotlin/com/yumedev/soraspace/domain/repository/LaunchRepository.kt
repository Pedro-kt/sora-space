package com.yumedev.soraspace.domain.repository

import com.yumedev.soraspace.domain.model.SpaceLaunch

interface LaunchRepository {
    suspend fun getUpcomingLaunches(limit: Int = 6): List<SpaceLaunch>
}
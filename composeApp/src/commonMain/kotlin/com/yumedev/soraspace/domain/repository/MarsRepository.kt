package com.yumedev.soraspace.domain.repository

import com.yumedev.soraspace.domain.model.MarsPhoto

interface MarsRepository {
    suspend fun getLatestPhotos(rover: String): List<MarsPhoto>
}
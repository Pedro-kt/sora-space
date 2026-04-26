package com.yumedev.soraspace.domain.repository

import com.yumedev.soraspace.domain.model.EonetEvent

interface EonetRepository {
    suspend fun getOpenEvents(): List<EonetEvent>
}
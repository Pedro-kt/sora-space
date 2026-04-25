package com.yumedev.soraspace.domain.repository

import com.yumedev.soraspace.domain.model.Asteroid

interface AsteroidRepository {
    suspend fun getWeeklyFeed(): List<Asteroid>
}
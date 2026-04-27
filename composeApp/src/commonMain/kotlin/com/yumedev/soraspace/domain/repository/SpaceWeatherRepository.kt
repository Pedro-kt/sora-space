package com.yumedev.soraspace.domain.repository

import com.yumedev.soraspace.domain.model.SpaceWeather

interface SpaceWeatherRepository {
    suspend fun getSpaceWeather(): SpaceWeather
}
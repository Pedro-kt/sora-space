package com.yumedev.soraspace.domain.repository

import com.yumedev.soraspace.domain.model.Apod

interface ApodRepository {
    suspend fun getApodOfTheDay(): Apod
    suspend fun getRecentApods(): List<Apod>
}
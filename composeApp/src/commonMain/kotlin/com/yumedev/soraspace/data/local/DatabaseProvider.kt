package com.yumedev.soraspace.data.local

import com.yumedev.soraspace.data.repository.FavoritesRepositoryImpl
import com.yumedev.soraspace.database.SoraDatabase
import com.yumedev.soraspace.domain.repository.FavoritesRepository

object DatabaseProvider {
    private val database: SoraDatabase by lazy {
        SoraDatabase(DatabaseDriverFactory().createDriver())
    }

    val favoritesRepository: FavoritesRepository by lazy {
        FavoritesRepositoryImpl(database)
    }
}
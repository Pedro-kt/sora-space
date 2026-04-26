package com.yumedev.soraspace.presentation.search

import com.yumedev.soraspace.domain.model.Asteroid
import com.yumedev.soraspace.domain.model.EonetEvent

enum class SearchTab { ASTEROIDS, EARTH_EVENTS }

data class SearchUiState(
    val selectedTab: SearchTab = SearchTab.ASTEROIDS,
    val asteroidsState: AsteroidsState = AsteroidsState.Loading,
    val eventsState: EventsState = EventsState.Idle,
    val selectedCategory: String? = null
) {
    sealed interface AsteroidsState {
        data object Loading : AsteroidsState
        data class Success(val asteroids: List<Asteroid>, val dateRange: String) : AsteroidsState
        data class Error(val message: String) : AsteroidsState
    }

    sealed interface EventsState {
        data object Idle : EventsState
        data object Loading : EventsState
        data class Success(val events: List<EonetEvent>) : EventsState
        data class Error(val message: String) : EventsState
    }
}
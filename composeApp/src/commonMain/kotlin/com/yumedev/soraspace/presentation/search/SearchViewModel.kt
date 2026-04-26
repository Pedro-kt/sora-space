package com.yumedev.soraspace.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.soraspace.domain.repository.AsteroidRepository
import com.yumedev.soraspace.domain.repository.EonetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class SearchViewModel(
    private val asteroidRepository: AsteroidRepository,
    private val eonetRepository: EonetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init { loadAsteroids() }

    fun selectTab(tab: SearchTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        if (tab == SearchTab.EARTH_EVENTS && _uiState.value.eventsState is SearchUiState.EventsState.Idle) {
            loadEvents()
        }
    }

    fun selectCategory(categoryId: String?) {
        _uiState.update { it.copy(selectedCategory = categoryId) }
    }

    fun retryAsteroids() = loadAsteroids()
    fun retryEvents()    = loadEvents()

    private fun loadAsteroids() {
        viewModelScope.launch {
            _uiState.update { it.copy(asteroidsState = SearchUiState.AsteroidsState.Loading) }
            val today   = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            val endDate = today.plus(DatePeriod(days = 7))
            _uiState.update {
                it.copy(
                    asteroidsState = try {
                        SearchUiState.AsteroidsState.Success(
                            asteroids = asteroidRepository.getWeeklyFeed(),
                            dateRange = "$today – $endDate"
                        )
                    } catch (e: Exception) {
                        SearchUiState.AsteroidsState.Error(e.message ?: "Unknown error")
                    }
                )
            }
        }
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(eventsState = SearchUiState.EventsState.Loading) }
            _uiState.update {
                it.copy(
                    eventsState = try {
                        SearchUiState.EventsState.Success(eonetRepository.getOpenEvents())
                    } catch (e: Exception) {
                        SearchUiState.EventsState.Error(e.message ?: "Unknown error")
                    }
                )
            }
        }
    }
}
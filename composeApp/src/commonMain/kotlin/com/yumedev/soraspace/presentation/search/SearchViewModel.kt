package com.yumedev.soraspace.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yumedev.soraspace.domain.repository.AsteroidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

class SearchViewModel(
    private val repository: AsteroidRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init { load() }

    fun retry() = load()

    private fun load() {
        viewModelScope.launch {
            _uiState.value = SearchUiState.Loading
            _uiState.value = try {
                val today = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                val endDate = today.plus(DatePeriod(days = 7))
                val asteroids = repository.getWeeklyFeed()
                SearchUiState.Success(
                    asteroids = asteroids,
                    dateRange = "${today} – ${endDate}"
                )
            } catch (e: Exception) {
                SearchUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
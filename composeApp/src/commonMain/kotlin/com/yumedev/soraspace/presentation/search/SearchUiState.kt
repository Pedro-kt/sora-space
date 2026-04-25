package com.yumedev.soraspace.presentation.search

import com.yumedev.soraspace.domain.model.Asteroid

sealed interface SearchUiState {
    data object Loading : SearchUiState
    data class Success(val asteroids: List<Asteroid>, val dateRange: String) : SearchUiState
    data class Error(val message: String) : SearchUiState
}
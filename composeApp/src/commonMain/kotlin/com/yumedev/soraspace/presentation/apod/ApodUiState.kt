package com.yumedev.soraspace.presentation.apod

import com.yumedev.soraspace.domain.model.Apod

sealed class ApodUiState {
    data object Loading : ApodUiState()
    data class Success(
        val featured: Apod,
        val feed: List<Apod>
    ) : ApodUiState()
    data class Error(val message: String) : ApodUiState()
}
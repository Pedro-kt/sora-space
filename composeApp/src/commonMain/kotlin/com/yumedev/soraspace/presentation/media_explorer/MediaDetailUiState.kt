package com.yumedev.soraspace.presentation.media_explorer

sealed interface MediaDetailUiState {
    data object Loading : MediaDetailUiState
    data class Success(val assetUrl: String) : MediaDetailUiState
    data class Error(val message: String) : MediaDetailUiState
}
package com.yumedev.soraspace.presentation.mars

sealed interface MediaDetailUiState {
    data object Loading : MediaDetailUiState
    data class Success(val assetUrl: String) : MediaDetailUiState
    data class Error(val message: String) : MediaDetailUiState
}
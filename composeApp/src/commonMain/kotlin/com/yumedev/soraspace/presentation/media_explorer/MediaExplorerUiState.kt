package com.yumedev.soraspace.presentation.media_explorer

import com.yumedev.soraspace.domain.model.NasaMedia

sealed interface MediaExplorerUiState {
    data object Idle : MediaExplorerUiState
    data object Loading : MediaExplorerUiState
    data class Success(val items: List<NasaMedia>) : MediaExplorerUiState
    data class Error(val message: String) : MediaExplorerUiState
}
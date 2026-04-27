package com.yumedev.soraspace.presentation.home

import com.yumedev.soraspace.domain.model.SpaceWeather

data class HomeUiState(
    val spaceWeather: SpaceWeather? = null,
    val isLoading: Boolean = true,
    val hasError: Boolean = false
)
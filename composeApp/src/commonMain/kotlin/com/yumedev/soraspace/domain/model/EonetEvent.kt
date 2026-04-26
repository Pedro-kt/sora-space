package com.yumedev.soraspace.domain.model

data class EonetEvent(
    val id: String,
    val title: String,
    val categoryId: String,
    val categoryTitle: String,
    val isOpen: Boolean,
    val date: String
)
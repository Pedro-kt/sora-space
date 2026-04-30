package com.yumedev.soraspace.domain.model

data class SpaceLaunch(
    val id: String,
    val name: String,
    val net: String,
    val statusName: String,
    val statusAbbrev: String,
    val provider: String,
    val missionType: String,
    val padName: String,
    val location: String,
    val imageUrl: String?
)
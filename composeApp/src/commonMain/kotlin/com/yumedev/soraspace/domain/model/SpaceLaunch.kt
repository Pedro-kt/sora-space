package com.yumedev.soraspace.domain.model

data class SpaceLaunch(
    val id: String,
    val name: String,
    val net: String,
    val url: String,
    val windowStart: String?,
    val windowEnd: String?,
    val statusName: String,
    val statusAbbrev: String,
    val provider: String,
    val missionName: String,
    val missionDescription: String,
    val missionType: String,
    val orbit: String,
    val padName: String,
    val location: String,
    val imageUrl: String?
)
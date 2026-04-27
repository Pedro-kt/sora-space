package com.yumedev.soraspace.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DonkiFlrDto(
    @SerialName("flrID")          val flrID: String? = null,
    @SerialName("classType")      val classType: String? = null,
    @SerialName("peakTime")       val peakTime: String? = null,
    @SerialName("beginTime")      val beginTime: String? = null,
    @SerialName("sourceLocation") val sourceLocation: String? = null
)
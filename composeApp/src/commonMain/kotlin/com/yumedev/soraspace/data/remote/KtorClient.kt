package com.yumedev.soraspace.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

// Cada plataforma provee su propio engine (OkHttp en Android, Darwin en iOS)
expect fun createHttpClientEngine(): HttpClientEngine

// La configuración del cliente es 100% compartida en commonMain
fun createHttpClient(): HttpClient = HttpClient(createHttpClientEngine()) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}
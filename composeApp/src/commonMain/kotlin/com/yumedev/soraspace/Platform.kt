package com.yumedev.soraspace

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
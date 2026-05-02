package com.yumedev.soraspace.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.yumedev.soraspace.database.SoraDatabase

actual class DatabaseDriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(SoraDatabase.Schema, "sora.db")
}
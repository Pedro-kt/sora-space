package com.yumedev.soraspace.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.yumedev.soraspace.SoraApplication
import com.yumedev.soraspace.database.SoraDatabase

actual class DatabaseDriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(SoraDatabase.Schema, SoraApplication.appContext, "sora.db")
}
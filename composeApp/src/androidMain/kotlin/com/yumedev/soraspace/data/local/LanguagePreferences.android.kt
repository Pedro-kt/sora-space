package com.yumedev.soraspace.data.local

import android.content.Context
import com.yumedev.soraspace.SoraApplication
import com.yumedev.soraspace.domain.model.AppLanguage

actual class LanguagePreferences {
    private val prefs = SoraApplication.appContext
        .getSharedPreferences("sora_prefs", Context.MODE_PRIVATE)

    actual fun getLanguage(): AppLanguage {
        val code = prefs.getString(KEY, AppLanguage.English.code)
        return AppLanguage.entries.find { it.code == code } ?: AppLanguage.English
    }

    actual fun saveLanguage(language: AppLanguage) {
        prefs.edit().putString(KEY, language.code).apply()
    }

    companion object {
        private const val KEY = "language_code"
    }
}
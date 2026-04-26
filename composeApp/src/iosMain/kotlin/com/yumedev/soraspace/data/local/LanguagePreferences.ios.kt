package com.yumedev.soraspace.data.local

import com.yumedev.soraspace.domain.model.AppLanguage
import platform.Foundation.NSUserDefaults

actual class LanguagePreferences {
    private val defaults = NSUserDefaults.standardUserDefaults

    actual fun getLanguage(): AppLanguage {
        val code = defaults.stringForKey(KEY)
        return AppLanguage.entries.find { it.code == code } ?: AppLanguage.English
    }

    actual fun saveLanguage(language: AppLanguage) {
        defaults.setObject(language.code, forKey = KEY)
    }

    companion object {
        private const val KEY = "language_code"
    }
}
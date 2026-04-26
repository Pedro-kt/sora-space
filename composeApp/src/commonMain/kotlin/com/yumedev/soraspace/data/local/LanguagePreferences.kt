package com.yumedev.soraspace.data.local

import com.yumedev.soraspace.domain.model.AppLanguage

expect class LanguagePreferences() {
    fun getLanguage(): AppLanguage
    fun saveLanguage(language: AppLanguage)
}
package com.yumedev.soraspace.ui.strings

import androidx.compose.runtime.compositionLocalOf
import com.yumedev.soraspace.domain.model.AppLanguage

val LocalStrings = compositionLocalOf<Strings> { Strings.En }

val LocalCurrentLanguage = compositionLocalOf<AppLanguage> { AppLanguage.English }

val LocalLanguageSetter = compositionLocalOf<(AppLanguage) -> Unit> { {} }
package com.yumedev.soraspace

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.yumedev.soraspace.data.local.LanguagePreferences
import com.yumedev.soraspace.domain.model.AppLanguage
import com.yumedev.soraspace.presentation.navigation.SoraNavGraph
import com.yumedev.soraspace.ui.strings.LocalCurrentLanguage
import com.yumedev.soraspace.ui.strings.LocalLanguageSetter
import com.yumedev.soraspace.ui.strings.LocalStrings
import com.yumedev.soraspace.ui.strings.Strings
import com.yumedev.soraspace.ui.theme.SoraColors

private val SoraColorScheme = darkColorScheme(
    background     = SoraColors.Background,
    onBackground   = SoraColors.TextPrimary,
    surface        = SoraColors.Surface,
    onSurface      = SoraColors.TextPrimary,
    surfaceVariant = SoraColors.SurfaceHigh,
    primary        = SoraColors.Accent,
    onPrimary      = SoraColors.TextPrimary,
    secondary      = SoraColors.Accent,
    onSecondary    = SoraColors.TextPrimary,
    outline        = SoraColors.TextTertiary
)

@Composable
fun App() {
    val preferences = remember { LanguagePreferences() }
    var language by remember { mutableStateOf(preferences.getLanguage()) }

    val strings = remember(language) {
        when (language) {
            AppLanguage.English -> Strings.En
            AppLanguage.Spanish -> Strings.Es
        }
    }

    CompositionLocalProvider(
        LocalStrings         provides strings,
        LocalCurrentLanguage provides language,
        LocalLanguageSetter  provides { selected ->
            language = selected
            preferences.saveLanguage(selected)
        }
    ) {
        MaterialTheme(colorScheme = SoraColorScheme) {
            SoraNavGraph()
        }
    }
}
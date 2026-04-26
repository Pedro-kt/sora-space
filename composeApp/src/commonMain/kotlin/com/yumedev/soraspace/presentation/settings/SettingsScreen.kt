package com.yumedev.soraspace.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.yumedev.soraspace.domain.model.AppLanguage
import com.yumedev.soraspace.ui.strings.LocalCurrentLanguage
import com.yumedev.soraspace.ui.strings.LocalLanguageSetter
import com.yumedev.soraspace.ui.strings.LocalStrings
import com.yumedev.soraspace.ui.theme.SoraColors
import com.yumedev.soraspace.ui.theme.SoraType

enum class AppTheme { System, Light, Dark }

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val s               = LocalStrings.current
    val currentLanguage = LocalCurrentLanguage.current
    val setLanguage     = LocalLanguageSetter.current

    var theme by rememberSaveable { mutableStateOf(AppTheme.System) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoraColors.Background)
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Vertical))
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilledIconButton(
                onClick  = onBack,
                modifier = Modifier.size(36.dp),
                colors   = IconButtonDefaults.filledIconButtonColors(
                    containerColor = SoraColors.Surface,
                    contentColor   = SoraColors.TextPrimary
                )
            ) {
                Icon(
                    imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier           = Modifier.size(18.dp)
                )
            }
            Text(text = s.screenSettings, style = SoraType.Label)
        }

        Spacer(Modifier.height(32.dp))

        SettingsSectionHeader(s.sectionAppearance)
        Spacer(Modifier.height(8.dp))
        SettingsGroup {
            AppTheme.entries.forEachIndexed { index, option ->
                SettingsOptionRow(
                    label       = when (option) {
                        AppTheme.System -> s.themeSystem
                        AppTheme.Light  -> s.themeLight
                        AppTheme.Dark   -> s.themeDark
                    },
                    selected    = theme == option,
                    showDivider = index < AppTheme.entries.lastIndex,
                    onClick     = { theme = option }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        SettingsSectionHeader(s.sectionLanguage)
        Spacer(Modifier.height(8.dp))
        SettingsGroup {
            AppLanguage.entries.forEachIndexed { index, lang ->
                SettingsOptionRow(
                    label       = when (lang) {
                        AppLanguage.English -> s.langEnglish
                        AppLanguage.Spanish -> s.langSpanish
                    },
                    selected    = currentLanguage == lang,
                    showDivider = index < AppLanguage.entries.lastIndex,
                    onClick     = { setLanguage(lang) }
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        SettingsSectionHeader(s.sectionAbout)
        Spacer(Modifier.height(8.dp))
        SettingsGroup {
            SettingsInfoRow(label = s.settingVersion, value = "1.0.0")
            SettingsDivider()
            SettingsInfoRow(label = s.settingDataSource, value = "NASA Open APIs")
        }
    }
}

// ─── Componentes internos ─────────────────────────────────────────────────────

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text     = title,
        style    = SoraType.Label,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
}

@Composable
private fun SettingsGroup(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(SoraColors.Surface),
        content  = content
    )
}

@Composable
private fun SettingsOptionRow(
    label: String,
    selected: Boolean,
    showDivider: Boolean,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text  = label,
                style = SoraType.Body.copy(color = SoraColors.TextPrimary)
            )
            if (selected) {
                Icon(
                    imageVector        = Icons.Filled.Check,
                    contentDescription = null,
                    tint               = SoraColors.Accent,
                    modifier           = Modifier.size(18.dp)
                )
            }
        }
        if (showDivider) SettingsDivider()
    }
}

@Composable
private fun SettingsInfoRow(label: String, value: String) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 15.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(text = label, style = SoraType.Body.copy(color = SoraColors.TextPrimary))
        Text(text = value, style = SoraType.Body)
    }
}

@Composable
private fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(0.5.dp)
            .background(SoraColors.SurfaceHigh)
    )
}
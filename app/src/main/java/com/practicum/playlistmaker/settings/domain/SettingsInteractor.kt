package com.practicum.playlistmaker.settings.domain

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

interface SettingsInteractor {

    fun setSavedAppTheme()

    fun getThemeSettings():ThemeSettings

    fun updateThemeSetting(settings:ThemeSettings)

}
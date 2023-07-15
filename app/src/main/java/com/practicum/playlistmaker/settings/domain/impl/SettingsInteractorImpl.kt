package com.practicum.playlistmaker.settings.domain.impl

import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository):
    SettingsInteractor {

    override fun setSavedAppTheme() {
         settingsRepository.setSavedAppTheme()
    }

    override fun getThemeSettings(): ThemeSettings {

         val themeDarkStatus = ThemeSettings()
             themeDarkStatus.isCheckedDarkTheme = settingsRepository.getThemeDarkStatus()

         return themeDarkStatus
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeDarkStatus(settings.isCheckedDarkTheme)

    }

}
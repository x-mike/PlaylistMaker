package com.practicum.playlistmaker.settings.domain

interface SettingsRepository {
    fun setSavedAppTheme()

    fun getThemeDarkStatus(): Boolean

    fun updateThemeDarkStatus(isChecked: Boolean?)

}
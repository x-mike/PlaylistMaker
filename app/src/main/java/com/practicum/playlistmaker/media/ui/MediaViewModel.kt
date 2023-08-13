package com.practicum.playlistmaker.media.ui

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class MediaViewModel(private val settingsInteractor:SettingsInteractor): ViewModel() {

    fun setSavedAppTheme(){
        settingsInteractor.setSavedAppTheme()
    }
}
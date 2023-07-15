package com.practicum.playlistmaker.main.ui

import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor

class MainViewModel(private val settingsInteractor:SettingsInteractor): ViewModel() {

    fun setSavedAppTheme(){
        settingsInteractor.setSavedAppTheme()
    }
}
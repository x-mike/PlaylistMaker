package com.practicum.playlistmaker.settings.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.settings.ui.model.SettingsState
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(private val settingsInteractor:SettingsInteractor,
                        private val sharingInteractor:SharingInteractor ): ViewModel() {

    private val stateThemeLiveData = MutableLiveData<SettingsState>()
    fun getThemeLiveData(): LiveData<SettingsState> = stateThemeLiveData

    fun getThemeSettings(){
        if(settingsInteractor.getThemeSettings().isCheckedDarkTheme == true){
            renderState(SettingsState.NIGHT_MODE)
        }
        else{
            renderState(SettingsState.DAY_MODE)
        }
    }

    fun updateThemeSetting(isChecked: Boolean){
        settingsInteractor.updateThemeSetting(ThemeSettings(isChecked))
    }

    fun shareApp(){
        sharingInteractor.shareApp()
    }

    fun openTerms(){
        sharingInteractor.openTerms()
    }

    fun writeSupport(){
        sharingInteractor.writeSupport()
    }

    private fun renderState(state:SettingsState){
        stateThemeLiveData.postValue(state)
    }

}
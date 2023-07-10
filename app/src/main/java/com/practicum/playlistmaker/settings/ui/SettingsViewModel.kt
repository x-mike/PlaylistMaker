package com.practicum.playlistmaker.settings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.settings.ui.model.SettingsState

class SettingsViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val settingsInteractor = Creator.provideInteractorSettings(application)
    private val sharingInteractor = Creator.provideInteracorSharing(application)

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
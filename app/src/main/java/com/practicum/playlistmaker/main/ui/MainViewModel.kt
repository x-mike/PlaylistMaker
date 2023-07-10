package com.practicum.playlistmaker.main.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator

class MainViewModel(application: Application): AndroidViewModel(application) {

   companion object{
      fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
          initializer {
              MainViewModel(this[APPLICATION_KEY] as Application)
          }
      }
   }
    private val settingsInteractor = Creator.provideInteractorSettings(application)

    fun setSavedAppTheme(){
        settingsInteractor.setSavedAppTheme()
    }
}
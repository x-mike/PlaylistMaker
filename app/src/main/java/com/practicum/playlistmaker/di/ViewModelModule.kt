package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.main.ui.MainViewModel
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.ui.TrackSearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModule {
    val viewModelModule = module {

        viewModel{
            MainViewModel(get())
        }
        viewModel {
            PlayerViewModel(get())
        }
        viewModel {
            TrackSearchViewModel(get())
        }
        viewModel {
            SettingsViewModel(get(),get())
        }
    }
}
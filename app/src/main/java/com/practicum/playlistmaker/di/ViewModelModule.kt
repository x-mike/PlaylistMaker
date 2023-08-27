package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.ui.MediaViewModel
import com.practicum.playlistmaker.media.ui.FavoriteTracksViewModel
import com.practicum.playlistmaker.media.ui.PlaylistTracksViewModel
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.search.ui.TrackSearchViewModel
import com.practicum.playlistmaker.settings.ui.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

class ViewModelModule {
    val viewModelModule = module {

        viewModel{
            MediaViewModel(get())
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
        viewModel{
            FavoriteTracksViewModel()
        }
        viewModel{
            PlaylistTracksViewModel()
        }
    }
}
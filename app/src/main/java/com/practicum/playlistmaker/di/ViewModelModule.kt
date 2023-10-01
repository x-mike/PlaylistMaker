package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media.ui.MediaViewModel
import com.practicum.playlistmaker.favorite.ui.FavoriteTracksViewModel
import com.practicum.playlistmaker.playlist.ui.PlaylistTracksViewModel
import com.practicum.playlistmaker.player.ui.PlayerViewModel
import com.practicum.playlistmaker.playlist.ui.NewPlaylistViewModel
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
            PlayerViewModel(get(),get(),get())
        }
        viewModel {
            TrackSearchViewModel(get())
        }
        viewModel {
            SettingsViewModel(get(),get())
        }
        viewModel{
            FavoriteTracksViewModel(get())
        }
        viewModel{
            PlaylistTracksViewModel(get())
        }
        viewModel{
            NewPlaylistViewModel(get())
        }
    }
}
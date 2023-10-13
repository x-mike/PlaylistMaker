package com.practicum.playlistmaker.di

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.practicum.playlistmaker.favorite.data.impl.FavoriteRepositoryImpl
import com.practicum.playlistmaker.favorite.domain.FavoriteRepository
import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.playlist.data.impl.PlaylistRepositoryImpl
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class RepositoryModule {
    val repositoryModule =
        module {

            single<TrackRepository>{
                TrackRepositoryImpl(get(),get(),get())
            }

            factory<PlayerRepository>{
                PlayerRepositoryImpl(androidContext(),get())
            }

            single<SettingsRepository>{
                SettingsRepositoryImpl(get())
            }

            single<ExternalNavigator>{
                ExternalNavigatorImpl(androidContext(),get())
            }

            single<FavoriteRepository>{
                FavoriteRepositoryImpl(get())
            }

            single<PlaylistRepository> {
                PlaylistRepositoryImpl(get(),get())
            }

            // MediaPlayer for PlayerRepositoryImpl
            factory {
                MediaPlayer()
            }

            //SharedPref for SittingsRepositoryImpl
            single {
                val SAVE_SETTINGS ="save_settings"

                androidContext().getSharedPreferences(SAVE_SETTINGS,Context.MODE_PRIVATE)
            }

            //Intent for ExternalNavigatorImpl
            single{
                Intent()
            }

        }
}
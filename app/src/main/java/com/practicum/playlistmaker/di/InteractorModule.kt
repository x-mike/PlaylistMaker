package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favorite.domain.FavoriteInteractor
import com.practicum.playlistmaker.favorite.domain.impl.FavoriteInteractorImpl
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

class InteractorModule {
    val iteractorModule = module {

        factory<PlayerInteractor> {
            PlayerInteractorImpl(get())
        }

        single<TrackInteractor>{
            TrackInteractorImpl(get())
        }

        single<SettingsInteractor> {
            SettingsInteractorImpl(get())
        }

        single<SharingInteractor>{
            SharingInteractorImpl(get())
        }

        single<FavoriteInteractor>{
            FavoriteInteractorImpl(get())
        }
    }
}
package com.practicum.playlistmaker.creator

import android.content.Context
import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.practicum.playlistmaker.search.data.local.impl.LocalStorageImpl
import com.practicum.playlistmaker.search.data.network.impl.NetworkClientImpl
import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl


object Creator {

    fun provideInteractorSearch(context: Context): TrackInteractor {
        return TrackInteractorImpl(
            TrackRepositoryImpl(
                NetworkClientImpl(),
                LocalStorageImpl(context)
            )
        )
    }

    fun provideInteractorSettings(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(SettingsRepositoryImpl(context))
    }

    fun provideInteracorSharing(context: Context): SharingInteractor{
        return SharingInteractorImpl(ExternalNavigatorImpl(context))
    }

    fun provideInteractorPlayer(context: Context): PlayerInteractor {
        return PlayerInteractorImpl(PlayerRepositoryImpl(context))
    }
}
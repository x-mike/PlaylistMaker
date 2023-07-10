package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.impl.PlayerInteractorImpl

object Creator {

    fun provideInteractorPlayer() : PlayerInteractor {

      return PlayerInteractorImpl(PlayerRepositoryImpl())

    }
}
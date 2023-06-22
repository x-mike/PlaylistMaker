package com.practicum.playlistmaker

import com.practicum.playlistmaker.data.PlayerRepositoryImpl
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.impl.MessageUiUseCaseImpl
import com.practicum.playlistmaker.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.presentation.api.ViewModel

object Creator {

    fun provideInteractorPlayerAndViewModel(viewModel: ViewModel) : PlayerInteractor {

      val playerRepository = PlayerRepositoryImpl(MessageUiUseCaseImpl(viewModel))

      val playerInteractorImpl =  PlayerInteractorImpl(playerRepository,viewModel)

      playerRepository.playerInteractor = playerInteractorImpl

      return playerInteractorImpl

    }
}
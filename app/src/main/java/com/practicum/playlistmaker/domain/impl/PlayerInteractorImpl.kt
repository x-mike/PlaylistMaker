package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.PlayerRepository
import com.practicum.playlistmaker.domain.models.StatePlayer
import com.practicum.playlistmaker.presentation.api.ViewModel

class PlayerInteractorImpl(val playerRepository: PlayerRepository, val viewModel: ViewModel) : PlayerInteractor {

    private var statePlayer = StatePlayer.STATE_DEFAULT


    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun preparePlayer(url: String?) {
        playerRepository.preparePlayer(url)
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    override fun setStatePlayer(stPlayer: StatePlayer) {
        statePlayer = stPlayer
    }

    override fun getStatePlayer(): StatePlayer = statePlayer

    override fun getCurrentPositionPlayer(): Int = playerRepository.getCurrentPositionPlayer()

    override fun onCompletionPlay() {
        viewModel.onCompletionPlay()
    }

}


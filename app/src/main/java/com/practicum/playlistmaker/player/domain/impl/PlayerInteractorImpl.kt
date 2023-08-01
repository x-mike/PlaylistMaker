package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.PlayerRepository
import com.practicum.playlistmaker.player.domain.model.PlayerState

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    var statePlayer = PlayerState.STATE_DEFAULT

    override fun startPlayer() {
        playerRepository.startPlayer()
        statePlayer = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
        statePlayer = PlayerState.STATE_PAUSED
    }

    override fun preparePlayer(url: String?, onCompletePlaying: () -> Unit) {
        if (statePlayer == PlayerState.STATE_DEFAULT) {
            playerRepository.preparePlayer(url)
            playerRepository.setListenersPlayer(
                { statePlayer = PlayerState.STATE_PREPARED },
                {
                    statePlayer = PlayerState.STATE_PREPARED
                    onCompletePlaying()
                })
        }
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
        statePlayer = PlayerState.STATE_DEFAULT
    }

    override fun getStatePlr() = statePlayer

    override fun getCurrentPositionPlayer(): Int = playerRepository.getCurrentPositionPlayer()

}


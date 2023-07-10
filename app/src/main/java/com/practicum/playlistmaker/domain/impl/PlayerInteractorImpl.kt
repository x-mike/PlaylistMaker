package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.PlayerRepository

class PlayerInteractorImpl(val playerRepository: PlayerRepository) : PlayerInteractor {


    override fun startPlayer() {
            playerRepository.startPlayer()
    }

    override fun pausePlayer() {
            playerRepository.pausePlayer()
    }

    override fun preparePlayer(url: String?,onCompleteListener:()->Unit) {
            playerRepository.preparePlayer(url, onCompleteListener)
    }

    override fun releasePlayer() {
            playerRepository.releasePlayer()
    }

    override fun getStatePlayer(onStateListener: (state: Int) -> Unit) {
            playerRepository.getStatePlayer(onStateListener)
    }

    override fun getCurrentPositionPlayer(): Int = playerRepository.getCurrentPositionPlayer()

    override fun setStatePlayer(state: Int) {
            playerRepository.setStatePlayer(state)
    }

}


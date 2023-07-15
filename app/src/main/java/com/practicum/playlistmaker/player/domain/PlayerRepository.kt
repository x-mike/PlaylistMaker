package com.practicum.playlistmaker.player.domain

interface PlayerRepository {

    fun preparePlayer(url: String?)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getCurrentPositionPlayer(): Int

    fun setListenersPlayer(
        onPrepared: () -> Unit,
        onCompleteListener: () -> Unit
    )
}
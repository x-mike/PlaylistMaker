package com.practicum.playlistmaker.domain.api

interface PlayerRepository {

    fun preparePlayer(url:String?)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getCurrentPositionPlayer(): Int
}
package com.practicum.playlistmaker.player.domain

import com.practicum.playlistmaker.player.domain.model.PlayerState

interface PlayerInteractor {

    fun preparePlayer(url:String?, onCompletePlaying:() -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getStatePlr():PlayerState

    fun getCurrentPositionPlayer (): Int

}
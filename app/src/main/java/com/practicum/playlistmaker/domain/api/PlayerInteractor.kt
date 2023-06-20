package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.StatePlayer

interface PlayerInteractor {

    fun preparePlayer(url:String?)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getStatePlayer():StatePlayer

    fun setStatePlayer(stPlayer: StatePlayer)

    fun getCurrentPositionPlayer (): Int

    fun onCompletionPlay()


}
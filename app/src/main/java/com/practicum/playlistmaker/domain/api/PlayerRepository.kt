package com.practicum.playlistmaker.domain.api

interface PlayerRepository {

    fun preparePlayer(url:String?,onCompleteListener:()->Unit)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getCurrentPositionPlayer(): Int

    fun getStatePlayer(onStateListener: (state:Int)->Unit)

    fun setStatePlayer(state: Int)
}
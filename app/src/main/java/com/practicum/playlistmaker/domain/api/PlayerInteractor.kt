package com.practicum.playlistmaker.domain.api

interface PlayerInteractor {

    fun preparePlayer(url:String?,onCompleteListener:()->Unit)

    fun startPlayer()

    fun pausePlayer()

    fun releasePlayer()

    fun getStatePlayer(onStateListener: (state:Int)->Unit)

    fun setStatePlayer(state: Int)

    fun getCurrentPositionPlayer (): Int

}
package com.practicum.playlistmaker.data

import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlistmaker.domain.api.PlayerRepository
import java.io.FileNotFoundException

class PlayerRepositoryImpl() : PlayerRepository {

//In future will change on enum State
    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    var statePlr = STATE_DEFAULT

    val mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String?, onCompleteListener: ()-> Unit) {

        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()

            mediaPlayer.setOnPreparedListener{
            statePlr = STATE_PREPARED
            }

            mediaPlayer.setOnCompletionListener {

            statePlr = STATE_PREPARED
            onCompleteListener()

            }
        }
        //Hardcode, replace in the future
        catch (exc:FileNotFoundException){
               Log.d("ErrorPlayer","Нет такого файла или каталога!")
        }

    }

    override fun startPlayer() {
        statePlr = STATE_PLAYING
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        statePlr = STATE_PAUSED
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPositionPlayer(): Int = mediaPlayer.currentPosition

    override fun getStatePlayer(onStateListener: (state: Int) -> Unit) {
       onStateListener(statePlr)
    }

    override fun setStatePlayer(state: Int) {
        statePlr = state
    }
}

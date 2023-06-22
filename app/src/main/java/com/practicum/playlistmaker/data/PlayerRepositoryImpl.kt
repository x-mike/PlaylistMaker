package com.practicum.playlistmaker.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.api.MessageUiUseCase
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.PlayerRepository
import com.practicum.playlistmaker.domain.models.StatePlayer
import java.io.FileNotFoundException

class PlayerRepositoryImpl(val messageUiUseCase: MessageUiUseCase,
                           var playerInteractor: PlayerInteractor? = null) : PlayerRepository {

    val mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String?) {

        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()

            mediaPlayer.setOnPreparedListener{
            playerInteractor!!.setStatePlayer(StatePlayer.STATE_PREPARED)
            }

            mediaPlayer.setOnCompletionListener {

            playerInteractor!!.setStatePlayer(StatePlayer.STATE_PREPARED)
            playerInteractor!!.onCompletionPlay()

            }
        }
        catch (exc:FileNotFoundException){
               messageUiUseCase.showToastError()
        }

    }

    override fun startPlayer() {
        playerInteractor!!.setStatePlayer(StatePlayer.STATE_PLAYING)
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        playerInteractor!!.setStatePlayer(StatePlayer.STATE_PAUSED)
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPositionPlayer(): Int = mediaPlayer.currentPosition

}

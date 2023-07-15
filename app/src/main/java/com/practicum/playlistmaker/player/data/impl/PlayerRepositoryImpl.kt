package com.practicum.playlistmaker.player.data.impl

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.PlayerRepository
import java.io.FileNotFoundException

class PlayerRepositoryImpl(private val context: Context,
                           private val mediaPlayer:MediaPlayer) : PlayerRepository {


    override fun preparePlayer(url: String?) {

        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
        } catch (exc: FileNotFoundException) {
            Log.e("ErrorPlayer", context.getString(R.string.playback_error))
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPositionPlayer(): Int = mediaPlayer.currentPosition

    override fun setListenersPlayer(
        onPrepared: () -> Unit,
        onCompleteListener: () -> Unit
    ) {

        mediaPlayer.setOnPreparedListener {
            onPrepared()
        }

        mediaPlayer.setOnCompletionListener {

           onCompleteListener()

        }
    }
}

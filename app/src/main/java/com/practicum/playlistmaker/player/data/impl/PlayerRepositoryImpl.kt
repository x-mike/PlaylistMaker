package com.practicum.playlistmaker.player.data.impl

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.PlayerRepository
import java.io.FileNotFoundException

class PlayerRepositoryImpl(val context: Context) : PlayerRepository {

    val mediaPlayer = MediaPlayer()

    override fun preparePlayer(url: String?) {

        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepareAsync()
        } catch (exc: FileNotFoundException) {
            Log.d("ErrorPlayer", context.getString(R.string.playback_error))
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

package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorite.domain.FavoriteInteractor
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.ui.model.TrackPlr
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    companion object {
        private const val DELAY_UPDATE_TIMER_MS = 300L
    }

    private val stateLiveData = MutableLiveData<PlayerStateRender>()
    fun getLiveData(): LiveData<PlayerStateRender> = stateLiveData

    private val favoriteStateLiveData = MutableLiveData<PlayerStateFavorite>()
    fun getFavoriteLiveData(): LiveData<PlayerStateFavorite> = favoriteStateLiveData

    private var timerJob: Job? = null


    fun preparePlayer(previewUrl: String) {
        playerInteractor.preparePlayer(previewUrl) {
            renderState(PlayerStateRender.STATE_PREPARED)
            cancelJobTimer()
        }
        renderState(PlayerStateRender.STATE_PREPARED)
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerStateRender.STATE_PLAYING)
        startTimer()
    }

    private fun pausePlayer() {
        playerInteractor.pausePlayer()
        renderState(PlayerStateRender.STATE_PAUSED)
        cancelJobTimer()
    }

    fun playbackControl() {
        if (playerInteractor.getStatePlr() == PlayerState.STATE_PLAYING) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun onPauseActivityPlayer() {
        pausePlayer()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.releasePlayer()
        cancelJobTimer()
    }

    fun getDateFormat(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(playerInteractor.getCurrentPositionPlayer())
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {

            while (playerInteractor.getStatePlr() == PlayerState.STATE_PLAYING) {

                delay(DELAY_UPDATE_TIMER_MS)
                renderState(PlayerStateRender.STATE_PLAYING)

            }
        }
    }

    fun addToFavorite(track:TrackPlr){
        viewModelScope.launch {
            favoriteInteractor.insertTrack(TrackPlr.mappingTrack(track))
            controlFavoriteState(true)
        }
    }

    fun delInFavorite(idTrack:Long?){
        viewModelScope.launch {
            favoriteInteractor.deleteTrack(idTrack)
            controlFavoriteState(false)
        }
    }

    fun controlFavoriteState(key: Boolean){
        if(key)renderFavoriteState(PlayerStateFavorite.IN_FAVORITE_STATE)
        else renderFavoriteState(PlayerStateFavorite.NOT_IN_FAVORITE_STATE)
    }

    private fun cancelJobTimer() {
        timerJob?.cancel()
    }


    private fun renderState(state: PlayerStateRender) {
        stateLiveData.postValue(state)
    }

    private fun renderFavoriteState(state:PlayerStateFavorite){
        favoriteStateLiveData.postValue(state)
    }

}




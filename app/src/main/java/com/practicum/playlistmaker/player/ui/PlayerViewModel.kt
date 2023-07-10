package com.practicum.playlistmaker.player.ui

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.player.domain.model.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(application: Application): AndroidViewModel(application) {

    companion object{

        private const val DELAY_UPDATE_TIMER_MS = 500L

        fun getViewModelFactory():ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

   private val playerInteractor = Creator.provideInteractorPlayer(application)

   private val handler = Handler(Looper.getMainLooper())

   private val stateLiveData = MutableLiveData<PlayerStateRender>()
   fun getLiveData():LiveData<PlayerStateRender> = stateLiveData

    val runnableTask = object : Runnable {
        override fun run() {

                if (playerInteractor.getStatePlr() == PlayerState.STATE_PLAYING ) {

                    renderState(PlayerStateRender.STATE_PLAYING)

                    handler.postDelayed(this, DELAY_UPDATE_TIMER_MS)
                }
            }
        }

    fun preparePlayer (previewUrl: String){
        playerInteractor.preparePlayer(previewUrl){
        renderState(PlayerStateRender.STATE_PREPARED)
        }
        renderState(PlayerStateRender.STATE_PREPARED)
    }

    private fun startPlayer(){
        playerInteractor.startPlayer()
        renderState(PlayerStateRender.STATE_PLAYING)
        updateTimerTrack()
    }

    private fun pausePlayer(){
        playerInteractor.pausePlayer()
        renderState(PlayerStateRender.STATE_PAUSED)
        handler.removeCallbacks(runnableTask)
    }

    fun playbackControl(){
        if(playerInteractor.getStatePlr() == PlayerState.STATE_PLAYING){
            pausePlayer()
        }
        else{
            startPlayer()
        }
    }

    fun onPauseActivityPlayer(){
         pausePlayer()
         handler.removeCallbacks(runnableTask)
    }

    fun onDestroyActivityPlayer(){
        playerInteractor.setStatePlr(PlayerState.STATE_DEFAULT)
        handler.removeCallbacks(runnableTask)
    }

     fun getDateFormat(): String {
         return SimpleDateFormat("mm:ss", Locale.getDefault())
             .format(playerInteractor.getCurrentPositionPlayer())
     }

    private fun updateTimerTrack() {
        handler.postDelayed(runnableTask, DELAY_UPDATE_TIMER_MS)
    }

    fun renderState(state:PlayerStateRender){
        stateLiveData.postValue(state)
    }
 }




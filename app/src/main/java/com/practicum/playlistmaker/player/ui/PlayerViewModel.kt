package com.practicum.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorite.domain.FavoriteInteractor
import com.practicum.playlistmaker.player.domain.PlayerInteractor
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.ui.model.TrackPlr
import com.practicum.playlistmaker.player.ui.states.PlayerStateFavorite
import com.practicum.playlistmaker.player.ui.states.PlayerStateRender
import com.practicum.playlistmaker.player.ui.states.PlayerToastState
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.models.EmptyStatePlaylist
import com.practicum.playlistmaker.playlist.domain.models.StateAddDb
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    companion object {
        private const val DELAY_UPDATE_TIMER_MS = 300L
    }

    private val stateLiveData = MutableLiveData<PlayerStateRender>()
    fun getLiveData(): LiveData<PlayerStateRender> = stateLiveData

    private val favoriteStateLiveData = MutableLiveData<PlayerStateFavorite>()
    fun getFavoriteLiveData(): LiveData<PlayerStateFavorite> = favoriteStateLiveData

    private val emptyPlaylistLiveData = MutableLiveData<EmptyStatePlaylist>()
    fun getEmptyPlaylistLiveData(): LiveData<EmptyStatePlaylist> = emptyPlaylistLiveData

    private val addPlaylistLivaData = MutableLiveData<StateAddDb>()
    fun getAddPlaylistLivaData(): LiveData<StateAddDb> = addPlaylistLivaData

    private val toastStateLivaData = MutableLiveData<PlayerToastState>()
    fun getToastStateLiveData():LiveData<PlayerToastState> = toastStateLivaData

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

    fun addToFavorite(track: TrackPlr) {
        viewModelScope.launch {
            favoriteInteractor.insertTrack(TrackPlr.mappingTrack(track))
            controlFavoriteState(true)
        }
    }

    fun delInFavorite(idTrack: Long?) {
        viewModelScope.launch {
            favoriteInteractor.deleteTrack(idTrack)
            controlFavoriteState(false)
        }
    }

    fun controlFavoriteState(key: Boolean) {
        if (key) renderFavoriteState(PlayerStateFavorite.IN_FAVORITE_STATE)
        else renderFavoriteState(PlayerStateFavorite.NOT_IN_FAVORITE_STATE)
    }

    private fun cancelJobTimer() {
        timerJob?.cancel()
    }

    fun getAllPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect {
                when (it) {
                    is EmptyStatePlaylist.EmptyPlaylist -> renderPlaylistState(it)
                    is EmptyStatePlaylist.NotEmptyPlaylist -> renderPlaylistState(it)
                }
            }
        }
    }

    fun addTrackInPlaylist(track:TrackPlr, playlist:Playlist) {

        if (track.trackId == null) {
            renderAddTrackState(StateAddDb.Error())
            return
        }

        viewModelScope.launch {

            if (playlist.tracksInPlaylist == null) {
                val stateError = playlistInteractor.addTrackInPlaylist(
                    TrackPlr.mappingTrack(track),
                    playlist.id!!
                )
                when (stateError) {
                    is StateAddDb.Error -> renderAddTrackState(StateAddDb.Error())
                    is StateAddDb.NoError -> renderAddTrackState(StateAddDb.NoError(playlist.playlistName))
                    is StateAddDb.Match -> renderAddTrackState(StateAddDb.Error())
                    is StateAddDb.NoData -> renderAddTrackState(StateAddDb.NoData())
                }
            } else {

                if (playlist.tracksInPlaylist.contains(track.trackId!!)) {
                    renderAddTrackState(StateAddDb.Match(playlist.playlistName))
                } else {
                    val stateError = playlistInteractor.addTrackInPlaylist(
                        TrackPlr.mappingTrack(track),
                        playlist.id!!
                    )
                    when (stateError) {
                        is StateAddDb.Error -> renderAddTrackState(StateAddDb.Error())
                        is StateAddDb.NoError -> renderAddTrackState(StateAddDb.NoError(playlist.playlistName))
                        is StateAddDb.Match -> renderAddTrackState(StateAddDb.Error())
                        is StateAddDb.NoData -> renderAddTrackState(StateAddDb.NoData())
                    }
                }
            }
        }
    }

    fun setStateNoDataPlaylistLivaData(){
        renderAddTrackState(StateAddDb.NoData())
    }

    fun showToast(message:String){
        renderToast(message)
    }

    fun setStateToastNone(){
        toastStateLivaData.postValue(PlayerToastState.NoneMessage)
    }

    private fun renderToast(message: String){
        toastStateLivaData.postValue(PlayerToastState.ShowMessage(message))
    }

    private fun renderState(state: PlayerStateRender) {
        stateLiveData.postValue(state)
    }

    private fun renderFavoriteState(state: PlayerStateFavorite) {
        favoriteStateLiveData.postValue(state)
    }

    private fun renderPlaylistState(state: EmptyStatePlaylist) {
        emptyPlaylistLiveData.postValue(state)
    }

    private fun renderAddTrackState(state: StateAddDb){
        addPlaylistLivaData.postValue(state)
    }
}




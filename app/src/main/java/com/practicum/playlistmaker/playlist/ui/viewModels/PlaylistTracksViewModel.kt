package com.practicum.playlistmaker.playlist.ui.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.ui.states.ToastState
import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.models.states.StateTracksInPlaylist
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.launch

open class PlaylistTracksViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private val statePlaylistLiveData = MutableLiveData<StateTracksInPlaylist>()
    fun getStatePlaylistLiveData(): LiveData<StateTracksInPlaylist> = statePlaylistLiveData

    private val toastStateLiveData = MutableLiveData<ToastState>()
    fun getToastStateLiveData(): LiveData<ToastState> = toastStateLiveData

    fun getPlaylist(idPlaylist: Int) {
        viewModelScope.launch {
            playlistInteractor.getPlaylist(idPlaylist).collect {
                renderStateTracksInPlaylist(it)
            }
        }
    }

    fun getTracksFromCommonTable(listIdTracks: ArrayList<Long>) {
        viewModelScope.launch {
            playlistInteractor.getTracksFromCommonTable(listIdTracks).collect {
                renderStateTracksInPlaylist(it)
            }
        }
    }

    fun deleteTrackFromPlaylist(idPlaylist: Int, idTrack: Long) {
        viewModelScope.launch {
            playlistInteractor.deleteTrackFromPlaylist(idPlaylist, idTrack).collect {
                renderStateTracksInPlaylist(it)
            }
        }
    }

    fun deletePlaylist(idPlaylist: Int) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylist(idPlaylist).collect {
                renderStateTracksInPlaylist(it)
            }
        }
    }

    fun showToast(message: String) {
        toastStateLiveData.postValue(ToastState.ShowMessage(message))
    }

    fun setStateToastNone() {
        toastStateLiveData.postValue(ToastState.NoneMessage)
    }

    fun sharePlaylist(playlistInMessage: String){
        sharingInteractor.sharePlaylist(playlistInMessage)
    }

    private fun renderStateTracksInPlaylist(state: StateTracksInPlaylist) {
        statePlaylistLiveData.postValue(state)
    }

}
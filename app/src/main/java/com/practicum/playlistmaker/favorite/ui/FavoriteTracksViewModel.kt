package com.practicum.playlistmaker.favorite.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.favorite.domain.FavoriteInteractor
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val favoriteInteractor: FavoriteInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>()
    fun getLiveData(): LiveData<FavoriteState> = stateLiveData


    fun getTracksList() {
        viewModelScope.launch {
            favoriteInteractor.getTracksList().collect{

                if (it?.isNotEmpty()==true) renderState(FavoriteState.ContentFavorite(it))
                else renderState(FavoriteState.EmptyFavorite())
            }
        }
    }

    private fun renderState(state: FavoriteState){
        stateLiveData.postValue(state)
    }
}
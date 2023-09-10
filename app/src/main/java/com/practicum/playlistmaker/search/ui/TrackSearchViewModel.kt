package com.practicum.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TrackSearchViewModel(private val trackInteractor:TrackInteractor) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchJob: Job? = null

    private val searchStateLiveData = MutableLiveData<TrackSearchState>()
    fun getLiveDataSearchState(): LiveData<TrackSearchState> = searchStateLiveData

    private var latestSearchRequest: String? = null
        set(value) {
            if (value == "") return else {
                field = value
            }
        }

    override fun onCleared() {
        searchJob?.cancel()
    }

    fun searchDebounce(searchRequest: String, errorConnecting: Boolean) {
        if (searchRequest == latestSearchRequest) return

        searchJob?.cancel()

        if (errorConnecting == false) {

            searchJob = viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY)
                doRequestSearch(searchRequest)
            }

        } else {

            searchJob = viewModelScope.launch {
                doRequestSearch(latestSearchRequest.toString())
            }

        }

        latestSearchRequest = searchRequest
    }

     fun doRequestSearch(searchRequest: String) {
        if (searchRequest.isNotEmpty()) {
            renderState(TrackSearchState.Loading())


            viewModelScope.launch {
                trackInteractor.doRequestSearch(searchRequest).collect {
                    processingResultSearch(it.first, it.second)
                }

            }

        }

    }

    private fun processingResultSearch(foundTracks: List<Track>?, statusError: Boolean?) {

        if (foundTracks != null) {

            when (foundTracks.isEmpty()) {
                true -> {
                    renderState(TrackSearchState.NotFound())
                }
                else -> {
                    renderState(TrackSearchState.Content(foundTracks))
                }

            }
        }
        if (statusError == true) {
            renderState(TrackSearchState.Error())
        }
    }

    fun setStateSavedSearch() {
        trackInteractor.getHistorySearch(object : TrackInteractor.HistoryTrackConsumer {
            override fun consume(savedTracks: List<Track>?) {

                if (savedTracks == null) {
                    renderState(TrackSearchState.Empty())
                } else {
                    when (savedTracks.isEmpty()) {
                        true -> {
                            renderState(TrackSearchState.Empty())
                        }
                        else -> {
                            renderState(TrackSearchState.ContentSavedSearch(savedTracks))
                        }
                    }
                }

            }
        })

    }

    fun setStateEmpty() {
        renderState(TrackSearchState.Empty())
    }

    fun clearHistorySearch() {
        trackInteractor.clearHistorySearch()
        renderState(TrackSearchState.Empty())
    }

    fun addTrackInHistory(track: Track) {
        trackInteractor.addTrackInHistory(track)
    }

    private fun renderState(trackSearchState: TrackSearchState) {
        searchStateLiveData.postValue(trackSearchState)
    }
}
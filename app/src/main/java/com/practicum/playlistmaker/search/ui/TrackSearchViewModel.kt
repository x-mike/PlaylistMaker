package com.practicum.playlistmaker.search.ui

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.models.Track

class TrackSearchViewModel(private val trackInteractor:TrackInteractor): ViewModel() {

    companion object {
       private const val SEARCH_DEBOUNCE_DELAY = 2000L
       private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler = Handler(Looper.getMainLooper())

    private val searchStateLiveData = MutableLiveData<TrackSearchState>()
    fun getLiveDataSearchState():LiveData<TrackSearchState> = searchStateLiveData

    private var latestSearchRequest: String? = null
        set(value) {
        if(value == "")return else{ field = value}
    }

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(searchRequest:String,errorConnecting: Boolean){
        if(searchRequest == latestSearchRequest) return

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        if(errorConnecting == false){

            val searchRunnable = Runnable {
                doRequestSearch(searchRequest)
            }

            val postTimeRunnable = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
            handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN,postTimeRunnable)
        }else{

            val searchRunnable = Runnable {
                doRequestSearch(latestSearchRequest.toString())
            }

            handler.post(searchRunnable)
        }

        latestSearchRequest = searchRequest
    }

    private fun doRequestSearch(searchRequest: String){
        if(searchRequest.isNotEmpty()){
            renderState(TrackSearchState.Loading())

            trackInteractor.doRequestSearch(searchRequest,object:TrackInteractor.TrackConsumer{
               //Executed not in the main thread. Used postValue, for transmission TrackSearchState
                override fun consume(foundTracks: List<Track>?, statusError: Boolean?) {

                    if(foundTracks != null){

                        when(foundTracks.isEmpty()){
                            true -> {renderState(TrackSearchState.NotFound())}
                            else -> {renderState(TrackSearchState.Content(foundTracks))}

                        }
                    }
                    if(statusError == true){
                        renderState(TrackSearchState.Error())
                    }

                }
            })

        }

    }

    fun setStateSavedSearch(){
        trackInteractor.getHistorySearch(object :TrackInteractor.HistoryTrackConsumer{
            override fun consume(savedTracks: List<Track>?) {

                 if (savedTracks == null){
                     renderState(TrackSearchState.Empty())
                 }else{
                     when (savedTracks.isEmpty()) {
                         true -> {renderState(TrackSearchState.Empty())}
                         else -> {renderState(TrackSearchState.ContentSavedSearch(savedTracks))}
                     }
                 }

            }
        })

    }

    fun setStateEmpty(){
        renderState(TrackSearchState.Empty())
    }

    fun clearHistorySearch(){
        trackInteractor.clearHistorySearch()
        renderState(TrackSearchState.Empty())
    }

    fun addTrackInHistory(track:Track){
        trackInteractor.addTrackInHistory(track)
    }

    private fun renderState(trackSearchState: TrackSearchState){
        searchStateLiveData.postValue(trackSearchState)
    }
}
package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.ResponseSearchState
import java.util.concurrent.Executors

class TrackInteractorImpl(val trackRepository: TrackRepository): TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun doRequestSearch(requestSearch: String, consumer: TrackInteractor.TrackConsumer) {

        executor.execute {
            when(val response = trackRepository.doRequestSearch(requestSearch)){
                is ResponseSearchState.Successful -> {consumer.consume(response.data,response.statusError)}
                is ResponseSearchState.Error -> {consumer.consume(response.data,response.statusError)}
             }
        }
    }

    override fun getHistorySearch(consumer: TrackInteractor.HistoryTrackConsumer) {
        consumer.consume(trackRepository.getHistorySearch())
    }

    override fun clearHistorySearch() {
        trackRepository.clearHistorySearch()
    }

    override fun addTrackInHistory(track: Track) {
        trackRepository.addTrackInHistory(track)
    }
}
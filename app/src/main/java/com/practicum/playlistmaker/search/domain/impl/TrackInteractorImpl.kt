package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.ResponseSearchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val trackRepository: TrackRepository) : TrackInteractor {

    override suspend fun doRequestSearch(requestSearch: String): Flow<Pair<List<Track>?, Boolean?>> {

        return trackRepository.doRequestSearch(requestSearch).map { response ->
            when (response) {
                is ResponseSearchState.Successful -> {
                    Pair(response.data, response.statusError)
                }
                is ResponseSearchState.Error -> {
                    Pair(response.data, response.statusError)
                }
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
package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.search.domain.TrackInteractor
import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.ResponseSearchState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TrackInteractorImpl(
    private val trackRepository: TrackRepository
) : TrackInteractor {


    override suspend fun doRequestSearch(requestSearch: String): Flow<Pair<List<Track>?, Boolean?>> {

        return trackRepository.doRequestSearch(requestSearch).map { response ->
            when (response) {
                is ResponseSearchState.Successful -> {
                    Pair(
                        checkOnFavorite(response.data),
                        response.statusError
                    )
                }
                is ResponseSearchState.Error -> {
                    Pair(response.data, response.statusError)
                }
            }
        }

    }

    override fun getHistorySearch(consumer: TrackInteractor.HistoryTrackConsumer) {

        checkHistoryOnFavorite(trackRepository.getHistorySearch()) {
            consumer.consume(it)
        }
    }

    override fun clearHistorySearch() {
        trackRepository.clearHistorySearch()
    }

    override fun addTrackInHistory(track: Track) {
        trackRepository.addTrackInHistory(track)
    }

    private fun checkHistoryOnFavorite(
        listTrack: List<Track>?,
        onCompletedResult: (List<Track>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {

            val idFromDb = trackRepository.getIdTracks()
            val mappedList: List<Track>?

            if (listTrack != null && idFromDb != null) {

                mappedList = listTrack.map { track ->
                    if (track.trackId in idFromDb) {
                        track.isFavorite = true
                    }
                    track
                }
                onCompletedResult.invoke(mappedList)
            } else {
                onCompletedResult.invoke(listTrack)
            }
        }
    }

    private suspend fun checkOnFavorite(listTrack: List<Track>?):List<Track>?{

        val idFromDb = trackRepository.getIdTracks()
        val mappedList: List<Track>?

        if (listTrack != null && idFromDb != null) {

            mappedList = listTrack.map { track ->
                if (track.trackId in idFromDb) {
                    track.isFavorite = true
                }
                track
            }
            return mappedList
        }
        return  listTrack
    }
}
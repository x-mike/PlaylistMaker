package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {

    suspend fun doRequestSearch(requestSearch:String): Flow<Pair<List<Track>?,Boolean?>>

    fun getHistorySearch(consumer:HistoryTrackConsumer)

    interface HistoryTrackConsumer {
        fun consume(savedTracks:List<Track>?)
    }

    fun clearHistorySearch()

    fun addTrackInHistory(track:Track)
}
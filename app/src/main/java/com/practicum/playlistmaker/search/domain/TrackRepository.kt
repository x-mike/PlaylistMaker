package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.ResponseSearchState
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    suspend fun doRequestSearch(requestSearch: String): Flow<ResponseSearchState<List<Track>>>

    fun getHistorySearch(): List<Track>

    fun clearHistorySearch()

    fun addTrackInHistory(track:Track)

}
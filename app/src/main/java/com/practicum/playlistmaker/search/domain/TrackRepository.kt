package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.ResponseSearchState

interface TrackRepository {
    fun doRequestSearch(requestSearch: String): ResponseSearchState<List<Track>>

    fun getHistorySearch(): List<Track>

    fun clearHistorySearch()

    fun addTrackInHistory(track:Track)

}
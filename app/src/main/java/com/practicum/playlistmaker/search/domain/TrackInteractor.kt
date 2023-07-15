package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.models.Track

interface TrackInteractor {

    fun doRequestSearch(requestSearch:String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks:List<Track>?, statusError:Boolean?)
    }


    fun getHistorySearch(consumer:HistoryTrackConsumer)

    interface HistoryTrackConsumer {
        fun consume(savedTracks:List<Track>?)
    }

    fun clearHistorySearch()

    fun addTrackInHistory(track:Track)
}
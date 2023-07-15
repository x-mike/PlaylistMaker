package com.practicum.playlistmaker.search.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed class TrackSearchState {

    data class Content(val trackList: List<Track>) : TrackSearchState()

    data class ContentSavedSearch(val trackList: List<Track>) : TrackSearchState()

    class Empty() : TrackSearchState()

    class NotFound() : TrackSearchState()

    class Error() : TrackSearchState()

    class Loading() : TrackSearchState()

}
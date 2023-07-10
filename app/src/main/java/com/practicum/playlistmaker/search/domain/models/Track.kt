package com.practicum.playlistmaker.search.domain.models

data class Track(
    var trackName: String? = null,
    var artistName: String? = null,
    var trackTimeMillis: Long? = null,
    var artworkUrl100: String? = null,
    var trackId: Long? = null,
    var collectionName: String? =null,
    var releaseDate: String? = null,
    var primaryGenreName: String? = null,
    var country: String? = null,
    var previewUrl: String? = null
)
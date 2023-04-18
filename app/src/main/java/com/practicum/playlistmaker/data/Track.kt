package com.practicum.playlistmaker.data

data class Track(
    var trackName: String? = null,
    var artistName: String? = null,
    var trackTimeMillis: Long? = null,
    var artworkUrl100: String? = null
)
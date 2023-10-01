package com.practicum.playlistmaker.playlist.domain.models

data class Playlist(
    val id: Int? = null,
    val playlistName: String? = null,
    val descriptionPlaylist: String? = null,
    val imageInStorage: String? = null,
    val tracksInPlaylist: ArrayList<Long>? = null,
    val countTracks: Int = 0
)

package com.practicum.playlistmaker.playlist.domain.models

sealed class StateAddDb {

    class NoError(val namePlaylist: String? = null):StateAddDb()

    class Error:StateAddDb()

    class Match(val namePlaylist: String? = null):StateAddDb()

}
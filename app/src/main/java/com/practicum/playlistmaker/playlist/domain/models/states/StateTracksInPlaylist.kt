package com.practicum.playlistmaker.playlist.domain.models.states

import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track

sealed class StateTracksInPlaylist {

    class NoTracks : StateTracksInPlaylist()

    data class WithTracks(val listTracks: List<Track>, val durationSumTime: Long) :
        StateTracksInPlaylist()

    data class DeletedTrack(
        val listTracks: List<Track>,
        val durationSumTime: Long,
        val counterTracks: Int
    ) : StateTracksInPlaylist()

    class ErrorStateTracks : StateTracksInPlaylist()

    class DeletedPlaylist: StateTracksInPlaylist()

    class InitPlaylist(val playlist: Playlist): StateTracksInPlaylist()
}

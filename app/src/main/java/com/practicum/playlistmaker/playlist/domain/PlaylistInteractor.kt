package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.playlist.domain.models.EmptyStatePlaylist
import com.practicum.playlistmaker.playlist.domain.models.StateAddDb
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getSavedImageFromPrivateStorage (uriFile: String?): String?

    suspend fun addPlaylist(playlist: Playlist):StateAddDb

    suspend fun addTrackInPlaylist(track: Track, idPlaylist:Int):StateAddDb

    suspend fun getAllPlaylists(): Flow<EmptyStatePlaylist>
}
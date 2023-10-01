package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getSavedImageFromPrivateStorage (uriFile: String?): String?

    suspend fun addPlaylist(playlist:Playlist,onAddPlaylistCallback: (Boolean) -> Unit)

    suspend fun addTrackInPlaylist(track:Track, idPlaylist:Int, onAddTrackCallback:(Boolean)->Unit)

    suspend fun getAllPlaylists(): Flow<List<Playlist>?>
}
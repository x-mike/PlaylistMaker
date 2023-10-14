package com.practicum.playlistmaker.playlist.domain

import com.practicum.playlistmaker.playlist.domain.models.states.EmptyStatePlaylist
import com.practicum.playlistmaker.playlist.domain.models.states.StateAddDb
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.playlist.domain.models.states.StateTracksInPlaylist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun getSavedImageFromPrivateStorage (uriFile: String?): String?

    suspend fun addPlaylist(playlist: Playlist): StateAddDb

    suspend fun addTrackInPlaylist(track: Track, idPlaylist:Int): StateAddDb

    suspend fun getAllPlaylists(): Flow<EmptyStatePlaylist>

    suspend fun getTracksFromCommonTable(listIdTracks: ArrayList<Long>):Flow<StateTracksInPlaylist>

    suspend fun deleteTrackFromPlaylist(idPlaylist:Int, idTrack:Long):Flow<StateTracksInPlaylist>

    suspend fun deletePlaylist(idPlaylist: Int):Flow<StateTracksInPlaylist>

    suspend fun getPlaylist(idPlaylist: Int):Flow<StateTracksInPlaylist>

    suspend fun updatePlaylist(idPlaylist: Int,
                               namePlaylist: String?,
                               descriptionPlaylist: String?,
                               imagePlaylist: String?): StateAddDb
}
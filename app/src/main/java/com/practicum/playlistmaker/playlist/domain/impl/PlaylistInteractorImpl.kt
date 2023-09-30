package com.practicum.playlistmaker.playlist.domain.impl

import com.practicum.playlistmaker.playlist.domain.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.playlist.domain.models.EmptyStatePlaylist
import com.practicum.playlistmaker.playlist.domain.models.StateAddDb
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistInteractorImpl(private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun addPlaylist(playlist: Playlist): StateAddDb {

        var stateAddDb: StateAddDb = StateAddDb.NoError()

        repository.addPlaylist(playlist) {
            if (!it) {
                stateAddDb = StateAddDb.Error()
            }
        }

        return stateAddDb
    }

    override suspend fun addTrackInPlaylist(track: Track, idPlaylist: Int): StateAddDb {

        var stateAddDb: StateAddDb = StateAddDb.NoError()

        repository.addTrackInPlaylist(track, idPlaylist) {
            if (!it) {
                stateAddDb = StateAddDb.Error()
            }
        }

        return stateAddDb
    }

    override suspend fun getAllPlaylists(): Flow<EmptyStatePlaylist> {

        return repository.getAllPlaylists().map { list ->

           if (list == null || list.isEmpty()){
               EmptyStatePlaylist.EmptyPlaylist()
           }
            else {
                EmptyStatePlaylist.NotEmptyPlaylist(list)
            }
         }
     }
}
package com.practicum.playlistmaker.playlist.data.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.playlist.data.db.PlaylistDataBase
import com.practicum.playlistmaker.playlist.data.db.models.PlaylistEntity
import com.practicum.playlistmaker.playlist.data.db.models.TrackEntityInPlaylist
import com.practicum.playlistmaker.playlist.data.storage.PlaylistStorage
import com.practicum.playlistmaker.playlist.domain.PlaylistRepository
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class PlaylistRepositoryImpl(
    private val database: PlaylistDataBase,
    private val gson: Gson,
    private val storage: PlaylistStorage
) : PlaylistRepository {

    companion object {
        private const val ONE_TRACK = 1
    }

    override suspend fun addPlaylist(playlist: Playlist, onAddPlaylistCallback: (Boolean) -> Unit) {
        withContext(Dispatchers.IO) {

            try {

                database.getPlaylistDao().insertPlaylist(
                    PlaylistEntity(
                        playlistName = playlist.playlistName,
                        descriptionPlaylist = playlist.descriptionPlaylist,
                        imageInStorage = playlist.imageInStorage
                    )
                )
                onAddPlaylistCallback(true)
            } catch (exp: Throwable) {
                onAddPlaylistCallback(false)
            }

        }
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>?> {
        return flow {
            emit(withContext(Dispatchers.IO) {
                val playlists = database.getPlaylistDao().getAllPlaylists()

                if (playlists != null) {

                    playlists.map {
                        Playlist(
                            id = it.id,
                            playlistName = it.playlistName,
                            descriptionPlaylist = it.descriptionPlaylist,
                            imageInStorage = it.imageInStorage,
                            tracksInPlaylist = takeFromJson(it.tracksInPlaylist),
                            countTracks = it.countTracks
                        )
                    }

                } else {
                    null
                }
            })
        }
    }

    override suspend fun addTrackInPlaylist(
        track: Track,
        idPlaylist: Int,
        onAddTrackCallback: (Boolean) -> Unit
    ) {

        withContext(Dispatchers.IO) {

            try {
                insertTrackInCommonTable(track)
                insertTrackInPlaylist(track, idPlaylist)

                onAddTrackCallback(true)

            } catch (exp: Throwable) {
                onAddTrackCallback(false)
            }
        }
    }

    private fun takeFromJson(jsonString: String?): ArrayList<Long> {

        if (jsonString != null) {
            val itemType = object : TypeToken<ArrayList<Long>>() {}.type

            return gson.fromJson(jsonString, itemType)
        }
        return ArrayList<Long>()
    }

    private fun toJsonFromArray(listId: ArrayList<Long>): String {
        return gson.toJson(listId)
    }

    private suspend fun insertTrackInCommonTable(track: Track) {

        database.getPlaylistDao().insertTrackInCommonTable(
            TrackEntityInPlaylist(
                trackId = track.trackId,
                isFavorite = track.isFavorite,
                trackName = track.trackName,
                artistName = track.artistName,
                trackTimeMillis = track.trackTimeMillis,
                artworkUrl100 = track.artworkUrl100,
                collectionName = track.collectionName,
                releaseDate = track.releaseDate,
                primaryGenreName = track.primaryGenreName,
                country = track.country,
                previewUrl = track.previewUrl
            )
        )
    }

    private suspend fun insertTrackInPlaylist(track: Track, idPlaylist: Int) {
        val playlist = database.getPlaylistDao().getPlaylist(idPlaylist)
        val listId = takeFromJson(playlist.tracksInPlaylist)

        listId.add(track.trackId!!)

        val newModifiedPlaylist = PlaylistEntity(
            id = playlist.id,
            playlistName = playlist.playlistName,
            descriptionPlaylist = playlist.descriptionPlaylist,
            imageInStorage = playlist.imageInStorage,
            tracksInPlaylist = toJsonFromArray(listId),
            countTracks = playlist.countTracks + ONE_TRACK

        )

        database.getPlaylistDao().updatePlaylist(newModifiedPlaylist)
    }

    override fun getSavedImageFromPrivateStorage(uriFile: String?): String? {
        return  storage.getSavedImageFromPrivateStorage(uriFile)
    }

}
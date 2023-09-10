package com.practicum.playlistmaker.favorite.data.impl

import com.practicum.playlistmaker.favorite.data.db.FavoriteDataBase
import com.practicum.playlistmaker.favorite.data.db.model.TrackEntity
import com.practicum.playlistmaker.favorite.domain.FavoriteRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class FavoriteRepositoryImpl(private val db: FavoriteDataBase): FavoriteRepository {

    override suspend fun insertTrack(track: Track) {
        withContext(Dispatchers.IO) {
            db.getTrackDao().insertTrack(
                TrackEntity(
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
    }

    override suspend fun deleteTrack(trackId: Long?) {
            withContext(Dispatchers.IO) {
                db.getTrackDao().deleteTrack(trackId)
            }

    }

    override suspend fun getTracksList(): Flow<List<Track>?> {
        return flow {
            emit(withContext(Dispatchers.IO) {

                val tracksList = db.getTrackDao().getTracksList()

                if (tracksList != null)
                {
                    tracksList.map { trackEntity ->
                    Track(
                        trackId = trackEntity.trackId,
                        isFavorite = trackEntity.isFavorite,
                        trackName = trackEntity.trackName,
                        artistName = trackEntity.artistName,
                        trackTimeMillis = trackEntity.trackTimeMillis,
                        artworkUrl100 = trackEntity.artworkUrl100,
                        collectionName = trackEntity.collectionName,
                        releaseDate = trackEntity.releaseDate,
                        primaryGenreName = trackEntity.primaryGenreName,
                        country = trackEntity.country,
                        previewUrl = trackEntity.previewUrl
                    )
                }

                }
                else{
                    null
                }
            })
        }
    }
}
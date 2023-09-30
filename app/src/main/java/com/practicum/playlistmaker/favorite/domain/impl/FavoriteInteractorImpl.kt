package com.practicum.playlistmaker.favorite.domain.impl

import com.practicum.playlistmaker.favorite.domain.FavoriteInteractor
import com.practicum.playlistmaker.favorite.domain.FavoriteRepository
import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteInteractorImpl(private val repository: FavoriteRepository): FavoriteInteractor {

    override suspend fun insertTrack(track: Track) {
        track.isFavorite = true
        repository.insertTrack(track)
    }

    override suspend fun deleteTrack(trackId: Long?) {
        repository.deleteTrack(trackId)
    }

    override suspend fun getTracksList(): Flow<List<Track>?> {

        return repository.getTracksList().map {
            it?.asReversed() ?: it
        }
    }

}
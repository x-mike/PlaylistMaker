package com.practicum.playlistmaker.favorite.domain

import com.practicum.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    suspend fun insertTrack(track: Track)

    suspend fun deleteTrack(trackId:Long?)

    suspend fun getTracksList(): Flow<List<Track>?>

}
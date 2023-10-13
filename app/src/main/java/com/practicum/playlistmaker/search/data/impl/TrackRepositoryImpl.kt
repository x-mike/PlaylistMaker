package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.favorite.data.db.FavoriteDataBase
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.storage.HistoryStorage
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.dto.ListTracksResponse
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.domain.TrackRepository
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.ResponseSearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val historyStorage: HistoryStorage,
    private val database: FavoriteDataBase
) : TrackRepository {

    override suspend fun doRequestSearch(requestSearch: String): Flow<ResponseSearchState<List<Track>>> =
        flow {
            val response = networkClient.doRequest(TrackSearchRequest(requestSearch))

            when (response.responseCode) {
                200 -> {
                    emit(
                        ResponseSearchState.Successful((response as ListTracksResponse)
                            .listTracks.map {
                                Track(
                                    it.trackId,
                                    it.isFavorite,
                                    it.trackName,
                                    it.artistName,
                                    it.trackTimeMillis,
                                    it.artworkUrl100,
                                    it.collectionName,
                                    it.releaseDate,
                                    it.primaryGenreName,
                                    it.country,
                                    it.previewUrl
                                )
                            })
                    )
                }
                else -> {
                    emit(ResponseSearchState.Error(statusError = true))
                }
            }
        }


    override fun addTrackInHistory(track: Track) {

        var isIdMatches: Boolean = false
        var indexItem: Int = -1

        val trackDto = TrackDto()

        trackDto.trackId = track.trackId
        trackDto.trackName = track.trackName
        trackDto.trackTimeMillis = track.trackTimeMillis
        trackDto.artistName = track.artistName
        trackDto.artworkUrl100 = track.artworkUrl100
        trackDto.collectionName = track.collectionName
        trackDto.country = track.country
        trackDto.previewUrl = track.previewUrl
        trackDto.primaryGenreName = track.primaryGenreName
        trackDto.releaseDate = track.releaseDate


        val tempListTracks: ArrayList<TrackDto> = historyStorage.getSavedHistorySearch()

        //Search for matches in the list
        tempListTracks.forEachIndexed { index, track ->
            if (track.trackId == trackDto.trackId) {
                isIdMatches = true
                indexItem = index
            }
        }
        //Adding to the list depending on the match
        if (isIdMatches) {
            tempListTracks.removeAt(indexItem)
            tempListTracks.add(0, trackDto)
        } else {
            tempListTracks.add(0, trackDto)
        }

        //The logic of selecting the first ten tracks
        if (tempListTracks.size <= 10) {
            historyStorage.addTrackListInHistory(tempListTracks)

        } else {
            historyStorage.addTrackListInHistory(tempListTracks.dropLast(tempListTracks.size - 10) as ArrayList<TrackDto>)
        }
    }

    override fun getHistorySearch(): List<Track> {
        return historyStorage.getSavedHistorySearch().map {
            Track(
                it.trackId,
                it.isFavorite,
                it.trackName,
                it.artistName,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.previewUrl
            )
        }
    }

    override fun clearHistorySearch() {
        historyStorage.clearHistorySearch()
    }

    override suspend fun getIdTracks():List<Long>?{

        return withContext(Dispatchers.IO) {
            database.getTrackDao().getIdTracks()
        }
        }
    }



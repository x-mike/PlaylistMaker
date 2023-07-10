package com.practicum.playlistmaker.player.ui.model

import android.os.Parcelable
import com.practicum.playlistmaker.search.domain.models.Track


@kotlinx.parcelize.Parcelize
class TrackPlr(
    var trackName: String? = null,
    var artistName: String? = null,
    var trackTimeMillis: Long? = null,
    var artworkUrl100: String? = null,
    var trackId: Long? = null,
    var collectionName: String? = null,
    var releaseDate: String? = null,
    var primaryGenreName: String? = null,
    var country: String? = null,
    var previewUrl: String? = null
) : Parcelable {

    companion object {
        fun mappingTrack(track: Track): TrackPlr {
            return TrackPlr(
                trackName = track.trackName,
                artistName = track.artistName,
                trackTimeMillis = track.trackTimeMillis,
                artworkUrl100 = track.artworkUrl100,
                trackId = track.trackId,
                collectionName = track.collectionName,
                releaseDate = track.releaseDate,
                primaryGenreName = track.primaryGenreName,
                country = track.country,
                previewUrl = track.previewUrl
            )
        }
    }

    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
}


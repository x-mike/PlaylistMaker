package com.practicum.playlistmaker.data

data class Track(
    var trackName: String? = null,
    var artistName: String? = null,
    var trackTimeMillis: Long? = null,
    var artworkUrl100: String? = null,
    var trackId: Long? = null,
    var collectionName: String? =null,
    var releaseDate: String? = null,
    var primaryGenreName: String? = null,
    var country: String? = null
)
{

    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")

}
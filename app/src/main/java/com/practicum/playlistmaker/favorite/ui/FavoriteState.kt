package com.practicum.playlistmaker.favorite.ui

import com.practicum.playlistmaker.search.domain.models.Track

sealed class FavoriteState {

    data class ContentFavorite(val listTack: List<Track>) : FavoriteState()

    class EmptyFavorite: FavoriteState()
}
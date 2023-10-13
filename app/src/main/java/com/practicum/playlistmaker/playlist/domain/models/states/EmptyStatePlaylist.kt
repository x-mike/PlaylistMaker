package com.practicum.playlistmaker.playlist.domain.models.states

import com.practicum.playlistmaker.playlist.domain.models.Playlist

sealed class EmptyStatePlaylist {
    class EmptyPlaylist: EmptyStatePlaylist()
    class NotEmptyPlaylist(val playlist: List<Playlist>): EmptyStatePlaylist()
}
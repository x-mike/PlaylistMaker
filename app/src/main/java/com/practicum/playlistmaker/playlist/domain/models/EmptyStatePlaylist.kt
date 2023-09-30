package com.practicum.playlistmaker.playlist.domain.models

sealed class EmptyStatePlaylist {
    class EmptyPlaylist: EmptyStatePlaylist()
    class NotEmptyPlaylist(val playlist: List<Playlist>): EmptyStatePlaylist()
}
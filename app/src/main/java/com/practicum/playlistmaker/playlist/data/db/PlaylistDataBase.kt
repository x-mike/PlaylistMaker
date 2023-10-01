package com.practicum.playlistmaker.playlist.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.playlist.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.playlist.data.db.models.PlaylistEntity
import com.practicum.playlistmaker.playlist.data.db.models.TrackEntityInPlaylist

@Database(version = 1, entities = [PlaylistEntity::class,TrackEntityInPlaylist::class])
abstract class PlaylistDataBase: RoomDatabase() {

    abstract fun getPlaylistDao(): PlaylistDao
}
package com.practicum.playlistmaker.favorite.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.favorite.data.db.dao.TrackDao
import com.practicum.playlistmaker.favorite.data.db.model.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class FavoriteDataBase : RoomDatabase() {

    abstract fun getTrackDao():TrackDao
}
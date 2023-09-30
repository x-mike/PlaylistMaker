package com.practicum.playlistmaker.favorite.data.db.dao

import androidx.room.OnConflictStrategy
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.practicum.playlistmaker.favorite.data.db.model.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track:TrackEntity)

    @Query("DELETE FROM track_table WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: Long?)

    @Query("SELECT * FROM track_table")
    suspend fun getTracksList():List<TrackEntity>?

    @Query("SELECT trackId FROM track_table")
    suspend fun getIdTracks(): List<Long>?

}
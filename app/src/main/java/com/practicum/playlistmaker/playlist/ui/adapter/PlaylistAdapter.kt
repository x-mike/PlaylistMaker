package com.practicum.playlistmaker.playlist.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.playlist.domain.models.Playlist


class PlaylistAdapter(
    private val listPlaylists: List<Playlist>,
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): PlaylistViewHolder {

        return PlaylistViewHolder(parentView)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {

        holder.bind(listPlaylists[position])

    }

    override fun getItemCount(): Int {
        return listPlaylists.size
    }
}
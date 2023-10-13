package com.practicum.playlistmaker.playlist.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.util.Formatter

class PlaylistViewHolder(
    parentView: ViewGroup,
    itemView: View = LayoutInflater.from(parentView.context)
        .inflate(R.layout.recycler_playlists, parentView, false)
) : RecyclerView.ViewHolder(itemView) {

    private val imagePlaylist: ImageView = itemView.findViewById(R.id.reclr_plst_image)
    private val playlistNameView: TextView = itemView.findViewById(R.id.reclr_plst_name)
    private val countTracksView: TextView = itemView.findViewById(R.id.reclr_plst_tracks)

    @SuppressLint("SuspiciousIndentation")
    fun bind(playlist: Playlist) {

        val roundCorners = RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.radius_button_low))

        val options = RequestOptions().transform(CenterCrop(),roundCorners)
           Glide.with(itemView)
            .load(playlist.imageInStorage)
            .placeholder(R.drawable.placeholder_45x45)
            .apply(options)
            .into(imagePlaylist)

        playlistNameView.text = playlist.playlistName
        countTracksView.text = Formatter.formattingTheEndTracks(playlist.countTracks,itemView.context)
    }
}
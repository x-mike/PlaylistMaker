package com.practicum.playlistmaker.player.ui.adapter

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

class PlaylistPlayerViewHolder(
    parentView: ViewGroup,
    itemView: View = LayoutInflater.from(parentView.context)
        .inflate(R.layout.recycler_add_playlist, parentView, false)
) : RecyclerView.ViewHolder(itemView){

    private val imagePlaylist: ImageView = itemView.findViewById(R.id.reclr_add_plst_image)
    private val playlistNameView: TextView = itemView.findViewById(R.id.reclr_add_plst_name)
    private val countTracksView: TextView = itemView.findViewById(R.id.reclr_add_plst_tracks)

    fun bind(playlist: Playlist) {

        val roundCorners = RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.radius_button_low))

        val options = RequestOptions().transform(CenterCrop(),roundCorners)

        Glide.with(itemView)
            .load(playlist.imageInStorage)
            .placeholder(R.drawable.placeholder_45x45)
            .apply(options)
            .into(imagePlaylist)

        playlistNameView.text = playlist.playlistName
        countTracksView.text = formattingTheEnd(playlist.countTracks)
    }

    private fun formattingTheEnd(countTracks: Int): String {

        if (countTracks in 11..14) {
            return "$countTracks ${itemView.context.getString(R.string.option_one_for_many_tracks)}"
        }

        return when(countTracks % 10){
            1 -> "$countTracks ${itemView.context.getString(R.string.option_one_for_one_track)}"
            2,3,4 ->  "$countTracks ${itemView.context.getString(R.string.option_two_for_one_track)}"
            else -> "$countTracks ${itemView.context.getString(R.string.option_one_for_many_tracks)}"
        }
    }
}
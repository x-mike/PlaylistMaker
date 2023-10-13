package com.practicum.playlistmaker.search.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Formatter


class TracksViewHolder(
    parentView: ViewGroup,
    itemView: View = LayoutInflater.from(parentView.context)
        .inflate(R.layout.recycler_item, parentView, false)
) : RecyclerView.ViewHolder(itemView) {

    private val trackNameView: TextView = itemView.findViewById(R.id.track_name_in_recycler)
    private val artistNameView: TextView = itemView.findViewById(R.id.artist_name_in_recycler)
    private val trackTimeView: TextView = itemView.findViewById(R.id.time_in_recycler)
    private val posterImageView: ImageView = itemView.findViewById(R.id.recycler_item_image)

    fun bind(trackData: Track) {
        trackNameView.text = trackData.trackName
        artistNameView.text = trackData.artistName
        trackTimeView.text = Formatter.dateFormatting(trackData.trackTimeMillis)

        Glide.with(itemView)
            .load(trackData.artworkUrl100)
            .placeholder(R.drawable.placeholder_45x45)
            .centerCrop()
            .transform(RoundedCorners(itemView
                .context
                .resources
                .getDimensionPixelSize(R.dimen.radius_corner_RcklView)))
            .into(posterImageView)
    }
}
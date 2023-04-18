package com.practicum.playlistmaker.logicRecyclers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.Track
import java.text.SimpleDateFormat
import java.util.*

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
        trackTimeView.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(trackData.trackTimeMillis)

        Glide.with(itemView)
            .load(trackData.artworkUrl100)
            .placeholder(R.drawable.placeholder_45x45)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(posterImageView)
    }
}
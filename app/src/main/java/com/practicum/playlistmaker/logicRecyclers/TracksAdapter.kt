package com.practicum.playlistmaker.logicRecyclers

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.data.Track

class TracksAdapter(private val listTrack: List<Track>) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): TracksViewHolder {

        return TracksViewHolder(parentView)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {

        holder.bind(listTrack[position])

    }

    override fun getItemCount(): Int {
        return listTrack.size
    }

}
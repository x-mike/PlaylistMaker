package com.practicum.playlistmaker.logicRecyclers

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.PlayerActivity
import com.practicum.playlistmaker.SearchHistory
import com.practicum.playlistmaker.data.Track

class TracksAdapter(private val listTrack: List<Track>, private val searchHistory: SearchHistory? = null) : RecyclerView.Adapter<TracksViewHolder>() {

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): TracksViewHolder {

        return TracksViewHolder(parentView)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {

        holder.bind(listTrack[position])

             holder.itemView.setOnClickListener {

                 if (searchHistory != null) {
                     searchHistory.addTrackInHistory(listTrack[position])
                 }

                 val intent = Intent(it.context,PlayerActivity::class.java)

                 intent.putExtra("dataTrack",Gson().toJson(listTrack[position]))

                 it.context.startActivity(intent)

            }

    }

    override fun getItemCount(): Int {
        return listTrack.size
    }

}
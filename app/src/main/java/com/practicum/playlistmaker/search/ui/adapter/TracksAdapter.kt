package com.practicum.playlistmaker.search.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.search.domain.models.Track
import android.os.Handler

class TracksAdapter(private val listTrack: List<Track>,
                    private val handler: Handler,
                    private val listener: TrackClickListener
) : RecyclerView.Adapter<TracksViewHolder>() {

companion object{
    private const val CLICK_DEBOUNCE_DELAY = 1000L
}

    private var isClickAllowed = true

    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): TracksViewHolder {

        return TracksViewHolder(parentView)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {

        holder.bind(listTrack[position])

             holder.itemView.setOnClickListener {

                 if (clickDebounce()) {

                 listener.onClickView(listTrack[position])

                 }
             }
    }

    override fun getItemCount(): Int {
        return listTrack.size
    }

    private fun clickDebounce(): Boolean {

        val isCurrentAllowedClick = isClickAllowed

        if (isClickAllowed){
            isClickAllowed = false

            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }

        return isCurrentAllowedClick
    }

    interface TrackClickListener {
        fun onClickView(track:Track)
    }
}
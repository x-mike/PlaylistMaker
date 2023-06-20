package com.practicum.playlistmaker.presentation.ui.search

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.presentation.ui.player.PlayerActivity
import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.domain.models.Track
import android.os.Handler

class TracksAdapter(private val listTrack: List<Track>,
                    private val handler: Handler,
                    private val searchHistory: SearchHistory? = null) : RecyclerView.Adapter<TracksViewHolder>() {

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

                     if (searchHistory != null) {
                         searchHistory.addTrackInHistory(listTrack[position])
                     }

                     val intent = Intent(it.context, PlayerActivity::class.java)

                     intent.putExtra("dataTrack", listTrack[position])

                     it.context.startActivity(intent)

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
}
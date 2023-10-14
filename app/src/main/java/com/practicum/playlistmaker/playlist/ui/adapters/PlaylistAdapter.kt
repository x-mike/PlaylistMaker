package com.practicum.playlistmaker.playlist.ui.adapters

import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistAdapter(
    private val listPlaylists: List<Playlist>,
    private val lifecycleScope:() -> LifecycleCoroutineScope,
    private val listener: PlaylistClickListener
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true
    override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): PlaylistViewHolder {

        return PlaylistViewHolder(parentView)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {

        holder.bind(listPlaylists[position])

        holder.itemView.setOnClickListener{

            if(clickDebounce()){

                listener.onClickView(listPlaylists[position])

            }
        }

    }

    override fun getItemCount(): Int {
        return listPlaylists.size
    }

    private fun clickDebounce(): Boolean {

        val isCurrentAllowedClick = isClickAllowed

        if (isClickAllowed) {
            isClickAllowed = false

            lifecycleScope.invoke().launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }

        return isCurrentAllowedClick
    }

    interface PlaylistClickListener {
        fun onClickView(playlist: Playlist)
    }
}
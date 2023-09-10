package com.practicum.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistTracksFragment: Fragment() {

    companion object{
        fun newInstance() = PlaylistTracksFragment()
    }
    //ViewModel while not used
    private val playlistTracksViewModel: PlaylistTracksViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistTracksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistTracksBinding.inflate(inflater,container,false)
        return binding.root
    }
}
package com.practicum.playlistmaker.playlist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistTracksBinding
import com.practicum.playlistmaker.util.ArgsTransfer
import com.practicum.playlistmaker.main.ui.RootActivity
import com.practicum.playlistmaker.playlist.domain.models.EmptyStatePlaylist
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.playlist.ui.adapter.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistTracksFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistTracksFragment()
    }

    private val playlistArray = ArrayList<Playlist>()
    private lateinit var playlistAdapter: PlaylistAdapter

    private val playlistTracksViewModel: PlaylistTracksViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistTracksBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistTracksViewModel.getEmptyStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }
        playlistAdapter = PlaylistAdapter(playlistArray)
        binding.recyclerPlaylists.adapter = playlistAdapter
        binding.recyclerPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.newPlaylist.setOnClickListener {

            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment)

        }

        playlistTracksViewModel.getAllPlaylists()
    }

    override fun onResume() {
        super.onResume()
        playlistTracksViewModel.getAllPlaylists()
        checkTransferredArgs()
    }

    private fun checkTransferredArgs() {

        val bundle = (requireActivity() as ArgsTransfer).getArgs()

        if (bundle != null) {
            val namePlaylist = bundle.getString(RootActivity.BUNDLE_ARGS)

            Toast.makeText(
                requireContext(),
                "${requireActivity().resources.getString(R.string.message_split_playlist_pOne)} $namePlaylist ${
                    requireActivity().resources.getString(
                        R.string.message_split_playlist_pTwo
                    )
                }", Toast.LENGTH_LONG
            ).show()

            (requireActivity() as ArgsTransfer).postArgs(null)
        }
    }

    private fun render(state: EmptyStatePlaylist) {
        when (state) {
            is EmptyStatePlaylist.EmptyPlaylist -> showEmptyState()
            is EmptyStatePlaylist.NotEmptyPlaylist -> showNotEmptyState(state.playlist)
        }
    }

    private fun showEmptyState() {
        binding.recyclerPlaylists.isVisible = false
        binding.noFoundLinearLayoutPl.isVisible = true
    }

    private fun showNotEmptyState(list: List<Playlist>) {
        binding.noFoundLinearLayoutPl.isVisible = false
        binding.recyclerPlaylists.isVisible = true

        playlistArray.clear()
        playlistArray.addAll(list)
        playlistAdapter.notifyDataSetChanged()

    }
}
package com.practicum.playlistmaker.playlist.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.util.ArgsTransfer
import com.practicum.playlistmaker.main.ui.RootActivity
import com.practicum.playlistmaker.playlist.domain.models.states.EmptyStatePlaylist
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.playlist.ui.viewModels.PlaylistsViewModel
import com.practicum.playlistmaker.playlist.ui.adapters.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

    private val playlistArray = ArrayList<Playlist>()
    private lateinit var playlistAdapter: PlaylistAdapter

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private lateinit var binding: FragmentPlaylistsBinding
    private lateinit var listnerAdapter: PlaylistAdapter.PlaylistClickListener


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistsViewModel.getEmptyStateLiveData().observe(viewLifecycleOwner) {
            render(it)
        }

        listnerAdapter = object : PlaylistAdapter.PlaylistClickListener{
            override fun onClickView(playlist: Playlist) {
                findNavController().navigate(R.id.action_mediaFragment_to_playlistTracksFragment,
                                             bundleOf(Pair(PlaylistTracksFragment.ID_ARG,playlist.id)))
            }
        }

        playlistAdapter = PlaylistAdapter(playlistArray,{lifecycleScope},listnerAdapter)
        binding.recyclerPlaylists.adapter = playlistAdapter
        binding.recyclerPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)

        binding.newPlaylist.setOnClickListener {

            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment)

        }

        playlistsViewModel.getAllPlaylists()
    }

    override fun onResume() {
        super.onResume()
        playlistsViewModel.getAllPlaylists()
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
package com.practicum.playlistmaker.playlist.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistTracksBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.player.ui.model.TrackPlr
import com.practicum.playlistmaker.player.ui.states.ToastState
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.playlist.domain.models.states.StateTracksInPlaylist
import com.practicum.playlistmaker.playlist.ui.viewModels.PlaylistTracksViewModel
import com.practicum.playlistmaker.playlist.ui.adapters.PlaylistTracksAdapter
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.util.Formatter
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistTracksFragment : Fragment() {

    companion object {
        const val ID_ARG = "ID_PLAYLIST"
    }

    private lateinit var binding: FragmentPlaylistTracksBinding
    private lateinit var bottomSheetTracks: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetMenu: BottomSheetBehavior<LinearLayout>
    private val arrayTracks = ArrayList<Track>()
    private lateinit var playlistTemp: Playlist
    private lateinit var listIdTracksTemp: ArrayList<Long>
    private var idTrackInPlaylist: Long? = null
    private var idPlaylist: Int? = null
    private lateinit var playlistTracksAdapter: PlaylistTracksAdapter
    private lateinit var listenerClickOnTracks: PlaylistTracksAdapter.ClickTrackListener

    private val playlistTracksViewModel: PlaylistTracksViewModel by viewModel()


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

        idPlaylist = requireArguments().getInt(ID_ARG)

        bottomSheetMenu = BottomSheetBehavior.from(binding.bottomSheetPlaylistMenu).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetTracks = BottomSheetBehavior.from(binding.bottomSheetPlaylistTracks)


        playlistTracksViewModel.getStatePlaylistLiveData().observe(viewLifecycleOwner) {
            renderTracksInPlaylist(it)
        }

        playlistTracksViewModel.getToastStateLiveData().observe(viewLifecycleOwner) {
            if (it is ToastState.ShowMessage) {
                ShowToast(it.message)
            }
            playlistTracksViewModel.setStateToastNone()
        }

         initCallbacks()

        bottomSheetTracks.setPeekHeight(getMetricsDisplayForPeekHight())

        playlistTracksAdapter =
            PlaylistTracksAdapter(arrayTracks, { lifecycleScope }, listenerClickOnTracks)
        binding.recyclerPlaylistTracks.adapter = playlistTracksAdapter
        binding.recyclerPlaylistTracks.layoutManager = LinearLayoutManager(requireContext())

        playlistTracksViewModel.getPlaylist(idPlaylist!!)

    }

    private fun getMetricsDisplayForPeekHight():Int{
        if(resources.configuration.fontScale>1.0){
            return requireActivity().windowManager.defaultDisplay.height*20/100
        }
        else{return requireActivity().windowManager.defaultDisplay.height*30/100}
    }

    private fun delTrackFromPlaylistWithDialog(): MaterialAlertDialogBuilder{
        val dialogDelete = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_del_track_dialog)
            .setMessage(R.string.message_about_delete_track)
            .setNeutralButton(R.string.cancel_playlist_dialog) { dialog, which ->

            }
            .setPositiveButton(R.string.positive_playlist_dialog) { dialog, which ->
                playlistTracksViewModel.deleteTrackFromPlaylist(idPlaylist!!, idTrackInPlaylist!!)
            }
        return dialogDelete
    }

    private fun deletePlaylistWithDialog(): MaterialAlertDialogBuilder{
        val dialogDeletePlaylist = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.title_del_playlist_dialog)
            .setMessage(R.string.message_about_delete_playlist)
            .setNeutralButton(R.string.cancel_playlist_dialog) { dialog, which ->

            }
            .setPositiveButton(R.string.positive_playlist_dialog) { dialog, which ->
                playlistTracksViewModel.deletePlaylist(idPlaylist!!)
            }
        return dialogDeletePlaylist
    }

    private fun initCallbacks(){

        listenerClickOnTracks = object : PlaylistTracksAdapter.ClickTrackListener {
            override fun onLongClickView(idTrack: Long?): Boolean {
                idTrackInPlaylist = idTrack
                delTrackFromPlaylistWithDialog().show()
                return true
            }

            override fun onShortClickView(track: Track) {
                val intent = Intent(requireContext(), PlayerActivity::class.java)
                intent.putExtra("dataTrack", TrackPlr.mappingTrack(track))
                requireContext().startActivity(intent)
            }
        }

        binding.menuPl.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.backButtonNewPlaylist.setOnClickListener {
            findNavController().popBackStack()
        }

        bottomSheetMenu.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                    else -> binding.overlay.isVisible = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = slideOffset
            }
        })

        binding.sharePl.setOnClickListener {
            sharePlaylist()
        }

        binding.playlistShare.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist()
        }

        binding.deletePlaylist.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
            deletePlaylistWithDialog().show()
        }

        binding.playlistEditInfo.setOnClickListener {

            findNavController().navigate(R.id.action_playlistTracksFragment_to_updatePlaylistFragment,
                bundleOf(Pair(UpdatePlaylistFragment.ID_PLAYLIST,idPlaylist),
                    Pair(UpdatePlaylistFragment.NAME_PLAYLIST,playlistTemp.playlistName),
                    Pair(UpdatePlaylistFragment.DESCRIPTION_PLAYLIST,playlistTemp.descriptionPlaylist),
                    Pair(UpdatePlaylistFragment.IMAGE_PLAYLIST,playlistTemp.imageInStorage)
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()

        bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun renderTracksInPlaylist(state: StateTracksInPlaylist) {
        when (state) {
            is StateTracksInPlaylist.NoTracks -> showStateNoTracks()
            is StateTracksInPlaylist.WithTracks -> showStateWithTracks(
                state.listTracks,
                state.durationSumTime
            )

            is StateTracksInPlaylist.DeletedTrack -> showStateDeletedTrack(
                state.listTracks,
                state.durationSumTime,
                state.counterTracks
            )

            is StateTracksInPlaylist.DeletedPlaylist -> showStateDeletedPlaylist()
            is StateTracksInPlaylist.InitPlaylist -> initPlaylist(state.playlist)
            is StateTracksInPlaylist.ErrorStateTracks -> Log.e(
                "ErrorQueryOnDb",
                getString(R.string.error_query_db)
            )
        }
    }

    private fun showStateNoTracks() {
        binding.recyclerPlaylistTracks.isVisible = false
        binding.emptyPl.isVisible = true
        binding.timeLengthPl.text = Formatter.formattingTheEndMinutes(null, requireContext())
        binding.tracksCounterPl.text = Formatter.formattingTheEndTracks(context = requireContext())
        binding.counterTracksMenu.text =
            Formatter.formattingTheEndTracks(context = requireContext())
    }

    private fun showStateWithTracks(listTracks: List<Track>, durationSumTime: Long) {
        binding.timeLengthPl.text =
            Formatter.formattingTheEndMinutes(durationSumTime, requireContext())
        arrayTracks.clear()
        arrayTracks.addAll(listTracks)
        playlistTracksAdapter.notifyDataSetChanged()
        binding.emptyPl.isVisible = false
        binding.recyclerPlaylistTracks.isVisible = true
    }

    private fun showStateDeletedTrack(
        listTracks: List<Track>,
        durationSumTime: Long,
        counterTracks: Int
    ) {
        binding.timeLengthPl.text =
            Formatter.formattingTheEndMinutes(durationSumTime, requireContext())
        binding.tracksCounterPl.text =
            Formatter.formattingTheEndTracks(counterTracks, requireContext())
        binding.counterTracksMenu.text =
            Formatter.formattingTheEndTracks(counterTracks, requireContext())
        arrayTracks.clear()
        arrayTracks.addAll(listTracks)
        playlistTracksAdapter.notifyDataSetChanged()
    }

    private fun showStateDeletedPlaylist() {
        findNavController().popBackStack()
    }

    private fun ShowToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun initPlaylist(playlist: Playlist) {

        playlistTemp = playlist
        listIdTracksTemp = playlist.tracksInPlaylist ?: ArrayList<Long>()

        val optionsForMainImagePl = RequestOptions().transform(CenterCrop())
        val roundCorners =
            RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.radius_corner_RcklView))
        val optionsForImagePl = RequestOptions().transform(CenterCrop(), roundCorners)

        Glide.with(this)
            .load(playlist.imageInStorage)
            .placeholder(R.drawable.placeholder_45x45)
            .apply(optionsForMainImagePl)
            .into(binding.imagePl)

        binding.namePl.text = playlist.playlistName
        binding.descriptionPl.text = playlist.descriptionPlaylist ?: ""
        binding.timeLengthPl.text = Formatter.formattingTheEndMinutes(null, requireContext())
        binding.tracksCounterPl.text =
            Formatter.formattingTheEndTracks(playlist.countTracks, requireContext())

        Glide.with(this)
            .load(playlist.imageInStorage)
            .placeholder(R.drawable.placeholder_45x45)
            .apply(optionsForImagePl)
            .into(binding.imagePlaylistMenu)

        binding.namePlaylistMenu.text = playlist.playlistName
        binding.counterTracksMenu.text =
            Formatter.formattingTheEndTracks(playlist.countTracks, requireContext())

        playlistTracksViewModel.getTracksFromCommonTable(listIdTracksTemp)
    }

    private fun sharePlaylist() {
        if (arrayTracks.isEmpty()) {
            playlistTracksViewModel.showToast(getString(R.string.message_no_tracks_in_playlist))
        } else {
            var sharePlaylist =
                "${binding.namePl.text}\n${binding.descriptionPl.text}\n${binding.tracksCounterPl.text}\n"

            for (i in 0 until arrayTracks.size) {
                sharePlaylist += "${i + 1}. ${arrayTracks[i].artistName} - ${arrayTracks[i].trackName}(${
                    Formatter.dateFormatting(
                        arrayTracks[i].trackTimeMillis
                    )
                })\n"
            }
            playlistTracksViewModel.sharePlaylist(sharePlaylist)
        }
    }
}
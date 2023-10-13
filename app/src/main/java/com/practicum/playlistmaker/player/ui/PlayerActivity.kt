package com.practicum.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.ui.adapter.PlaylistPlayerAdapter
import com.practicum.playlistmaker.player.ui.model.TrackPlr
import com.practicum.playlistmaker.player.ui.states.PlayerStateFavorite
import com.practicum.playlistmaker.player.ui.states.PlayerStateRender
import com.practicum.playlistmaker.player.ui.states.PlayerToastState
import com.practicum.playlistmaker.playlist.domain.models.EmptyStatePlaylist
import com.practicum.playlistmaker.playlist.domain.models.Playlist
import com.practicum.playlistmaker.playlist.domain.models.StateAddDb
import com.practicum.playlistmaker.playlist.ui.NewPlaylistFragment
import com.practicum.playlistmaker.util.ArgsTransfer
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity(), ArgsTransfer {

    companion object {
        const val BUNDLE_ARGS = "args"
    }

    private var bundleArgs: Bundle? = null
    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var dataTrack: TrackPlr
    private val playlists = ArrayList<Playlist>()
    private lateinit var playlistAdapter: PlaylistPlayerAdapter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var onClickPlaylistCallback: PlaylistPlayerAdapter.PlaylistClickListener

    private var savedTimeTrack: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getLiveData().observe(this) {
            render(it)
        }

        viewModel.getFavoriteLiveData().observe(this) {
            renderFavorite(it)
        }

        viewModel.getEmptyPlaylistLiveData().observe(this) {
            renderPlaylist(it)
        }

        viewModel.getAddPlaylistLivaData().observe(this) {
            renderAddTrackInPlaylist(it)
        }

        viewModel.getToastStateLiveData().observe(this){
            if(it is PlayerToastState.ShowMessage){
                showMessage(it.message)
                viewModel.setStateToastNone()
            }
        }

        initializingTrackData()

        viewModel.preparePlayer(dataTrack.previewUrl ?: "")

        val bottomSheetContainer = binding.bottomSheetPlaylist
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        setListenersForPlayer()

        playlistAdapter =
            PlaylistPlayerAdapter(playlists, { lifecycleScope }, onClickPlaylistCallback)
        binding.recyclerPlaylistPlayer.adapter = playlistAdapter
        binding.recyclerPlaylistPlayer.layoutManager = LinearLayoutManager(this)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence("play_time", viewModel.getDateFormat())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedTimeTrack = savedInstanceState.getCharSequence("play_time").toString()

    }

    override fun onPause() {
        super.onPause()

        viewModel.onPauseActivityPlayer()
    }

    private fun initializingTrackData() {

        //Make changing in future. Correct problem with "deprecated".
        dataTrack = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("dataTrack", TrackPlr::class.java) ?: TrackPlr()
        } else {
            intent.getParcelableExtra("dataTrack") ?: TrackPlr()
        }

        viewModel.controlFavoriteState(dataTrack.isFavorite)

        binding.trackName.text = dataTrack.trackName.orEmpty()
        binding.artistName.text = dataTrack.artistName.orEmpty()
        binding.timeTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(dataTrack.trackTimeMillis).orEmpty()

        binding.currentAlbum.text = dataTrack.collectionName.orEmpty()

        //Used SimpleDateFormat for get data from iTunes
        binding.currentYear.text = SimpleDateFormat("yyyy", Locale.getDefault())
            .parse(dataTrack.releaseDate?.replace("Z$", "+0000") ?: "")?.let {
                SimpleDateFormat(
                    "yyyy",
                    Locale.getDefault()
                ).format(
                    it
                )
            }

        binding.currentGenre.text = dataTrack.primaryGenreName.orEmpty()
        binding.currentCountry.text = dataTrack.country.orEmpty()

        Glide.with(this)
            .load(dataTrack.getCoverArtwork())
            .placeholder(R.drawable.placeholder_45x45)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.radius_corner_PlPoster)))
            .into(binding.poster)
    }

    private fun setListenersForPlayer() {
        binding.backButtonPlayer.setOnClickListener {
            finish()
        }

        binding.buttonAdd.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

            viewModel.getAllPlaylists()
        }

        binding.newPlaylistPlayer.setOnClickListener {
            supportFragmentManager.commit {

                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                replace(R.id.fragmentContainerPlayer, NewPlaylistFragment.newInstance(true))
                addToBackStack("player")
                setReorderingAllowed(true)
            }
        }

        onClickPlaylistCallback = object : PlaylistPlayerAdapter.PlaylistClickListener {
            override fun onClickView(playlist: Playlist) {
                viewModel.addTrackInPlaylist(dataTrack, playlist)
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

                binding.overlay.alpha = slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                    else -> binding.overlay.isVisible = true
                }
            }
        })


        binding.buttonHeart.setOnClickListener {
            if (dataTrack.isFavorite) {
                viewModel.delInFavorite(dataTrack.trackId)
                dataTrack.isFavorite = false
            } else {
                viewModel.addToFavorite(dataTrack)
                dataTrack.isFavorite = true
            }
        }

        binding.buttonPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        supportFragmentManager.addOnBackStackChangedListener {
            checkTransferredArgs()
        }
    }

    private fun render(state: PlayerStateRender) {
        when (state) {
            PlayerStateRender.STATE_PLAYING -> {
                binding.buttonPlay.background =
                    getDrawable(this.resources, R.drawable.button_pause, this.theme)
                binding.timePlayingTrack.text = viewModel.getDateFormat()
            }
            PlayerStateRender.STATE_PAUSED -> {
                binding.buttonPlay.background =
                    getDrawable(this.resources, R.drawable.button_play, this.theme)
            }
            PlayerStateRender.STATE_PREPARED, PlayerStateRender.STATE_DEFAULT -> {
                if (savedTimeTrack == "") {
                    binding.buttonPlay.background =
                        getDrawable(this.resources, R.drawable.button_play, this.theme)
                    binding.timePlayingTrack.text = getString(R.string.start_time)
                } else {
                    binding.buttonPlay.background =
                        getDrawable(this.resources, R.drawable.button_play, this.theme)
                    binding.timePlayingTrack.text = savedTimeTrack
                    savedTimeTrack = ""

                }
            }
        }
    }

    private fun renderFavorite(state: PlayerStateFavorite) {
        when (state) {
            PlayerStateFavorite.IN_FAVORITE_STATE -> binding.buttonHeart.background =
                getDrawable(this.resources, R.drawable.button_red_heart, this.theme)

            PlayerStateFavorite.NOT_IN_FAVORITE_STATE -> binding.buttonHeart.background =
                getDrawable(this.resources, R.drawable.button_heart, this.theme)
        }
    }

    private fun renderPlaylist(state: EmptyStatePlaylist) {
        when (state) {
            is EmptyStatePlaylist.EmptyPlaylist -> {
                binding.recyclerPlaylistPlayer.isVisible = false
            }
            is EmptyStatePlaylist.NotEmptyPlaylist -> {
                playlists.clear()
                playlists.addAll(state.playlist)
                playlistAdapter.notifyDataSetChanged()
                binding.recyclerPlaylistPlayer.isVisible = true
            }
        }
    }

    private fun renderAddTrackInPlaylist(state: StateAddDb) {
        when (state) {
            is StateAddDb.Match -> {
                viewModel.showToast("${getString(R.string.track_in_playlist)} ${state.namePlaylist}")
                viewModel.setStateNoDataPlaylistLivaData()
            }
            is StateAddDb.Error -> {
                Log.e("ErrorAddBd", getString(R.string.error_add_playlist))
            }
            is StateAddDb.NoError -> {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                viewModel.showToast("${getString(R.string.track_add_done)} ${state.namePlaylist}")
                viewModel.setStateNoDataPlaylistLivaData()
            }
            is StateAddDb.NoData ->{
                //State for Single Live Event, show Toast and other way
             }
        }
    }

    override fun getArgs(): Bundle? {
        return bundleArgs
    }

    override fun postArgs(args: Bundle?) {
        bundleArgs = args
    }

    private fun checkTransferredArgs() {

        if (getArgs() != null) {
            val namePlaylist = bundleArgs!!.getString(BUNDLE_ARGS)

            Toast.makeText(
                this,
                "${resources.getString(R.string.message_split_playlist_pOne)} $namePlaylist ${
                    resources.getString(R.string.message_split_playlist_pTwo)}",
                Toast.LENGTH_LONG
            ).show()

            postArgs(null)
        }
}
    private fun showMessage(message:String){
        Toast.makeText(
            this,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}
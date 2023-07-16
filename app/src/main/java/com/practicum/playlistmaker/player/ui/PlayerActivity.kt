package com.practicum.playlistmaker.player.ui

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.player.ui.model.TrackPlr
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel: PlayerViewModel by viewModel()
    private lateinit var dataTrack: TrackPlr

    private var savedTimeTrack: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getLiveData().observe(this) {
            render(it)
        }

        initializingTrackData()

        viewModel.preparePlayer(dataTrack.previewUrl ?: "")

        setListenersForPlayer()

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

        binding.buttonPlay.setOnClickListener {
            viewModel.playbackControl()
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

}
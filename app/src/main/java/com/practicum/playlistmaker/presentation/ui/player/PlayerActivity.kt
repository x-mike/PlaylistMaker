package com.practicum.playlistmaker.presentation.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val DELAY_UPDATE_TIMER_MS = 500L
    }

    val playerInteractor = Creator.provideInteractorPlayer()
    val handler = Handler(Looper.getMainLooper())

    val runnableTask = object : Runnable {
        override fun run() {

                playerInteractor.getStatePlayer {state ->
                    if (state == 2) {

                        timePlayTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                            .format(playerInteractor.getCurrentPositionPlayer())

                        handler.postDelayed(this, DELAY_UPDATE_TIMER_MS)
                    }
                }
        }
    }

    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTimeMillis: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var artworkUrl100: ImageView
    private lateinit var backButton: Button
    private lateinit var playButton: ImageButton
    private lateinit var dataTrack: Track
    private lateinit var timePlayTrack: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        initializingViews()

        initializingTrackData()

        //used lambda for get callback on complete listener
            playerInteractor.preparePlayer(dataTrack.previewUrl ?: ""){

        chengingDrawableButtonPlay()
        timePlayTrack.text = getString(R.string.start_time)
        }

        setListenersForPlayer()

    }

    override fun onPause() {
        super.onPause()

        pausePlayer()
        handler.removeCallbacks(runnableTask)

    }

    override fun onDestroy() {
        super.onDestroy()
    //set state 1 - DEFAULT
            playerInteractor.setStatePlayer(1)
        handler.removeCallbacks(runnableTask)

    }

    private fun startPlayer() {

            playerInteractor.startPlayer()

        chengingDrawableButtonPlay()

        updateTimerTrack()
    }

    private fun pausePlayer() {
            playerInteractor.pausePlayer()

        handler.removeCallbacks(runnableTask)

        chengingDrawableButtonPlay()

    }

    private fun playbackControl() {
           playerInteractor.getStatePlayer {state ->

           when (state) {
           2 -> {
               pausePlayer()
           }
           else -> {
               startPlayer()
           }
        }
       }


    }

    private fun updateTimerTrack() {

        handler.postDelayed(runnableTask, DELAY_UPDATE_TIMER_MS)

    }

    private fun initializingViews() {

        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        trackTimeMillis = findViewById(R.id.time_track)
        collectionName = findViewById(R.id.current_album)
        releaseDate = findViewById(R.id.current_year)
        primaryGenreName = findViewById(R.id.current_genre)
        country = findViewById(R.id.current_country)
        backButton = findViewById(R.id.back_button_player)
        artworkUrl100 = findViewById(R.id.poster)
        playButton = findViewById(R.id.button_play)
        timePlayTrack = findViewById(R.id.time_playing_track)
    }

    private fun initializingTrackData() {

        //Make changing in future. Correct problem with "deprecated".
        dataTrack = intent.getParcelableExtra("dataTrack") ?: Track()

        trackName.text = dataTrack.trackName.orEmpty()

        artistName.text = dataTrack.artistName.orEmpty()
        trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(dataTrack.trackTimeMillis)

        collectionName.text = dataTrack.collectionName.orEmpty()

        //Used SimpleDateFormat for get data from iTunes
        releaseDate.text = SimpleDateFormat("yyyy", Locale.getDefault())
            .parse(dataTrack.releaseDate?.replace("Z$", "+0000") ?: "")?.let {
                SimpleDateFormat(
                    "yyyy",
                    Locale.getDefault()
                ).format(
                    it
                )
            }

        primaryGenreName.text = dataTrack.primaryGenreName.orEmpty()
        country.text = dataTrack.country.orEmpty()

        Glide.with(this)
            .load(dataTrack.getCoverArtwork())
            .placeholder(R.drawable.placeholder_45x45)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.radius_corner_PlPoster)))
            .into(artworkUrl100)
    }

    private fun setListenersForPlayer() {
        backButton.setOnClickListener {
            finish()
        }

        playButton.setOnClickListener {
            playbackControl()
        }
    }


    private fun chengingDrawableButtonPlay() {
            playerInteractor.getStatePlayer(){state ->
            if(state == 2){
                playButton.background = getDrawable(this.resources, R.drawable.button_pause, this.theme)
            }
            else{
                playButton.background = getDrawable(this.resources, R.drawable.button_play, this.theme)
            }

        }

    }

}
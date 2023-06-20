package com.practicum.playlistmaker

import java.io.FileNotFoundException
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat.getDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.data.Track
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY_UPDATE_TIMER = 500L
    }

    private var playerState = STATE_DEFAULT

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
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var dataTrack: Track
    private lateinit var handler: Handler
    private lateinit var runnableTask: Runnable
    private lateinit var timePlayTrack: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        initializingViews()

        handler = Handler(Looper.getMainLooper())
        mediaPlayer = MediaPlayer()

        runnableTask = object : Runnable {
            override fun run() {

                if (playerState == STATE_PLAYING) {

                    timePlayTrack.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                        .format(mediaPlayer.currentPosition)

                    handler.postDelayed(this, DELAY_UPDATE_TIMER)

                }
            }

        }

        initializingTrackData()

        preparePlayer()

        setListenersForPlayer()

    }

    override fun onPause() {
        super.onPause()

        pausePlayer()
        handler.removeCallbacks(runnableTask)


    }

    override fun onDestroy() {
        super.onDestroy()

       playerState = STATE_DEFAULT
       handler.removeCallbacks(runnableTask)
       mediaPlayer.release()

    }

    private fun preparePlayer() {
        //checking null or empty for previewUrl
        try {
            mediaPlayer.setDataSource(dataTrack.previewUrl.orEmpty())

            mediaPlayer.prepareAsync()

            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PREPARED

            }

        } catch (exc: FileNotFoundException) {
            Toast.makeText(
                this,
                "Воспроизведение невозможно! Нет такого файла или каталога.",
                Toast.LENGTH_LONG
            )
                .show()
        }

    }

    private fun startPlayer() {
        playerState = STATE_PLAYING
        mediaPlayer.start()
        playButton.background = getDrawable(this.resources, R.drawable.button_stop, this.theme)
        updateTimerTrack()
    }

    private fun pausePlayer() {
        playerState = STATE_PAUSED
        mediaPlayer.pause()
        handler.removeCallbacks(runnableTask)
        playButton.background = getDrawable(this.resources, R.drawable.button_play, this.theme)

    }

    private fun playbackControl() {

        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun updateTimerTrack() {

        handler.postDelayed(runnableTask, DELAY_UPDATE_TIMER)

    }

    private fun initializingViews(){

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

    private fun initializingTrackData(){

        dataTrack = Gson()
            .fromJson(intent.getStringExtra("dataTrack"), Track::class.java)

        trackName.text = dataTrack.trackName

        artistName.text = dataTrack.artistName
        trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(dataTrack.trackTimeMillis)

        collectionName.text = dataTrack.collectionName.orEmpty()

        //used SimpleDateFormat for get data from iTunes
        releaseDate.text = SimpleDateFormat("yyyy", Locale.getDefault())
            .parse(dataTrack.releaseDate?.replace("Z$","+0000")?:"")?.let {
                SimpleDateFormat("yyyy",
                Locale.getDefault()).format(
                    it
                )
            }

        primaryGenreName.text = dataTrack.primaryGenreName
        country.text = dataTrack.country

        Glide.with(this)
            .load(dataTrack.getCoverArtwork())
            .placeholder(R.drawable.placeholder_45x45)
            .transform(RoundedCorners(resources.getDimensionPixelSize(R.dimen.radius_corner_PlPoster)))
            .into(artworkUrl100)
    }

    private fun setListenersForPlayer(){
        backButton.setOnClickListener {
            finish()
        }

        playButton.setOnClickListener {
            playbackControl()
        }

        mediaPlayer.setOnCompletionListener {
            playButton.background = getDrawable(this.resources, R.drawable.button_play, this.theme)
            timePlayTrack.text = getString(R.string.start_time)
            playerState = STATE_PREPARED
        }
    }

}
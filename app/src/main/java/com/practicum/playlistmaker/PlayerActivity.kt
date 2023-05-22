package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.data.Track
import java.text.SimpleDateFormat
import java.util.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTimeMillis: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var artworkUrl100: ImageView
    private lateinit var backButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        trackTimeMillis = findViewById(R.id.time_track)
        collectionName = findViewById(R.id.current_album)
        releaseDate = findViewById(R.id.current_year)
        primaryGenreName = findViewById(R.id.current_genre)
        country = findViewById(R.id.current_country)
        backButton = findViewById(R.id.back_button_player)
        artworkUrl100 = findViewById(R.id.poster)

        val dataTrack: Track = Gson()
            .fromJson(intent.getStringExtra("dataTrack"),Track::class.java)

        trackName.text = dataTrack.trackName

        artistName.text = dataTrack.artistName
        trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(dataTrack.trackTimeMillis)

        collectionName.text = if(dataTrack.collectionName == null) "" else dataTrack.collectionName
        releaseDate.text = dataTrack.releaseDate?.subSequence(0,4)

        primaryGenreName.text = dataTrack.primaryGenreName
        country.text = dataTrack.country

        Glide.with(this)
            .load(dataTrack.getCoverArtwork())
            .placeholder(R.drawable.placeholder_45x45)
            .transform(RoundedCorners(25))
            .into(artworkUrl100)

        backButton.setOnClickListener {
            finish()
        }

    }

}
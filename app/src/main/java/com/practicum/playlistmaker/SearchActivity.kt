package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.data.Track


class SearchActivity : AppCompatActivity() {

    companion object {
        const val KEY_SEARCH = "KEY_SEARCH"
        var textSearchField: CharSequence? = ""
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        val icSearchFieldErase = findViewById<ImageView>(R.id.icSearchFieldErase)
        val backButton = findViewById<Button>(R.id.back_button)
        val searchField = findViewById<EditText>(R.id.searchField)
        val recyclerViewTracks = findViewById<RecyclerView>(R.id.recyclerView_tracks)

        recyclerViewTracks.layoutManager = LinearLayoutManager(this)

        recyclerViewTracks.adapter = TracksAdapter(getListTracks())


        searchField.setText(textSearchField)


        icSearchFieldErase.setOnClickListener {
            searchField.setText("")

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }

        val searchFieldWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                icSearchFieldErase.visibility = doVisibilityIconDellText(s)
                textSearchField = searchField.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }

        searchField.addTextChangedListener(searchFieldWatcher)

        backButton.setOnClickListener {
            finish()
        }

    }

    fun doVisibilityIconDellText(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(KEY_SEARCH, textSearchField)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        textSearchField = savedInstanceState.getCharSequence(KEY_SEARCH)

    }

    fun getListTracks(): List<Track> {

        val listTracks = List(5) { Track() }

        listTracks.get(0).apply {
            trackName = "Smells Like Teen Spirit"
            artistName = "Nirvana"
            trackTime = "5:01"
            artworkUrl100 =
                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"
        }

        listTracks.get(1).apply {
            trackName = "Billie Jean"
            artistName = "Michael Jackson"
            artworkUrl100 =
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"
            trackTime = "4:35"
        }

        listTracks.get(2).apply {
            trackName = "Stayin' Alive"
            artistName = "Bee Gees"
            trackTime = "4:10"
            artworkUrl100 =
                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"
        }

        listTracks.get(3).apply {
            trackName = "Whole Lotta Love"
            artistName = "Led Zeppelin"
            trackTime = "5:33"
            artworkUrl100 =
                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"
        }

        listTracks.get(4).apply {
            trackName = "Sweet Child O`Mine"
            artistName = "Guns N` Roses"
            trackTime = "5:03"
            artworkUrl100 =
                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"
        }

        return listTracks
    }

    class TracksViewHolder(
        parentView: ViewGroup,
        itemView: View = LayoutInflater.from(parentView.context)
            .inflate(R.layout.recycler_item, parentView, false)
    ) : RecyclerView.ViewHolder(itemView) {

        private val trackNameView: TextView = itemView.findViewById(R.id.trackNameInRecycler)
        private val artistNameView: TextView = itemView.findViewById(R.id.artistNameInRecycler)
        private val trackTimeView: TextView = itemView.findViewById(R.id.timeInRecycler)
        private val posterImageView: ImageView = itemView.findViewById(R.id.recycler_item_image)

        fun bind(trackData: Track) {
            trackNameView.text = trackData.trackName
            artistNameView.text = trackData.artistName
            trackTimeView.text = trackData.trackTime
            Glide.with(itemView)
                .load(trackData.artworkUrl100)
                .placeholder(R.drawable.placeholder_45x45)
                .centerCrop()
                .transform(RoundedCorners(10))
                .into(posterImageView)
        }
    }

    class TracksAdapter(private val listTrack: List<Track>) : RecyclerView.Adapter<TracksViewHolder>() {

        override fun onCreateViewHolder(parentView: ViewGroup, viewType: Int): TracksViewHolder {

            return TracksViewHolder(parentView)
        }

        override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {

            holder.bind(listTrack[position])

        }

        override fun getItemCount(): Int {
            return listTrack.size
        }

    }

}
package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.data.ListTracksResponse
import com.practicum.playlistmaker.data.Track
import com.practicum.playlistmaker.httpRequests.ItunesApi
import com.practicum.playlistmaker.logicRecyclers.TracksAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    companion object {
        const val KEY_SEARCH = "KEY_SEARCH"
        var textSearchField: CharSequence? = ""
    }

    @SuppressLint("MissingInflatedId")

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ItunesApi::class.java)

    private val listTracks = ArrayList<Track>()

    private lateinit var icSearchFieldRemoveText: ImageView
    private lateinit var backButton: Button
    private lateinit var searchField: EditText
    private lateinit var recyclerViewTracks: RecyclerView
    private lateinit var noFoundLinear: LinearLayout
    private lateinit var updateButton: Button
    private lateinit var tracksAdapter: TracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        icSearchFieldRemoveText = findViewById(R.id.ic_search_field_remove_text)
        backButton = findViewById(R.id.back_button)
        searchField = findViewById(R.id.search_field)
        recyclerViewTracks = findViewById(R.id.recyclerView_tracks)
        noFoundLinear = findViewById(R.id.no_found_linearLayout)
        updateButton = findViewById(R.id.update_connect)
        tracksAdapter = TracksAdapter(listTracks)

        searchField.setText(textSearchField)

        recyclerViewTracks.adapter = tracksAdapter
        recyclerViewTracks.layoutManager = LinearLayoutManager(this)


        icSearchFieldRemoveText.setOnClickListener {
            searchField.setText("")

            listTracks.clear()
            tracksAdapter.notifyDataSetChanged()
            recyclerViewTracks.visibility = View.VISIBLE

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }

        val searchFieldWatcher = object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                icSearchFieldRemoveText.visibility = doVisibilityIconDellText(s)
                textSearchField = searchField.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }

        searchField.addTextChangedListener(searchFieldWatcher)

        searchField.setOnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                doRequestGetOnItunes()

                true
            }
            false
        }

        updateButton.setOnClickListener {
            doRequestGetOnItunes()
        }

        backButton.setOnClickListener {
            finish()
        }

    }

    private fun doVisibilityIconDellText(s: CharSequence?): Int {
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

    private fun doRequestGetOnItunes() {

        itunesService.search(searchField.text.toString())
            .enqueue(object : Callback<ListTracksResponse> {

                override fun onResponse(
                    call: Call<ListTracksResponse>,
                    response: Response<ListTracksResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.listTracks?.isNotEmpty() == true) {
                                listTracks.clear()
                                listTracks.addAll(response.body()?.listTracks!!)
                                tracksAdapter.notifyDataSetChanged()
                                recyclerViewTracks.visibility = View.VISIBLE

                            } else {
                                noFoundLinear.visibility = View.VISIBLE
                                recyclerViewTracks.visibility = View.GONE
                            }
                        }
                        else -> {
                            noFoundLinear.visibility = View.GONE
                            recyclerViewTracks.visibility = View.GONE
                        }
                    }

                }

                override fun onFailure(call: Call<ListTracksResponse>, t: Throwable) {
                    noFoundLinear.visibility = View.GONE
                    recyclerViewTracks.visibility = View.GONE

                }
            }

            )

    }

}
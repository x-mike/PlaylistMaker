package com.practicum.playlistmaker.presentation.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.data.dto.ListTracksResponse
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.data.network.ItunesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    companion object {
        const val KEY_SEARCH = "KEY_SEARCH"
        var textSearchField: CharSequence? = ""
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    //Interceptor for take logs about request http
    private val interceptorHttp = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptorHttp)
        .build()

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val itunesService = retrofit.create(ItunesApi::class.java)

    private val listTracks = ArrayList<Track>()
    private val listSearchHistory = ArrayList<Track>()

    private lateinit var icSearchFieldRemoveText: ImageView
    private lateinit var backButton: Button
    private lateinit var searchField: EditText
    private lateinit var recyclerViewTracks: RecyclerView
    private lateinit var noFoundLinear: LinearLayout
    private lateinit var updateButton: Button
    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var recyclerHistorySearch: RecyclerView
    private lateinit var historySearchLinear: LinearLayout
    private lateinit var noConnectLinear: LinearLayout
    private lateinit var searchHistory: SearchHistory
    private lateinit var historySearchAdapter: TracksAdapter
    private lateinit var delButtHistorySearch: Button
    private lateinit var progressBar: ProgressBar

    private val handler = Handler(Looper.getMainLooper())
    private val runnableTask = Runnable {
        doRequestGetOnItunes()
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initializingViews()

        searchHistory = SearchHistory(getSharedPreferences(App.SAVE_SETTINGS, MODE_PRIVATE))
        tracksAdapter = TracksAdapter(listTracks, handler, searchHistory)
        historySearchAdapter = TracksAdapter(listSearchHistory,handler)

        searchField.setText(textSearchField)

        recyclerViewTracks.adapter = tracksAdapter
        recyclerViewTracks.layoutManager = LinearLayoutManager(this)

        recyclerHistorySearch.adapter = historySearchAdapter
        recyclerHistorySearch.layoutManager = LinearLayoutManager(this)

        setListenersForSearch()

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

        if (searchField.text.isNotEmpty()) {
            recyclerViewTracks.visibility = View.GONE
            noFoundLinear.visibility = View.GONE
            noConnectLinear.visibility = View.GONE

            progressBar.visibility =View.VISIBLE

            itunesService.search(searchField.text.toString())
                .enqueue(object : Callback<ListTracksResponse> {

                    override fun onResponse(
                        call: Call<ListTracksResponse>,
                        response: Response<ListTracksResponse>
                    ) {
                        progressBar.visibility = View.GONE

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
                            404 -> {
                                Toast.makeText(
                                    applicationContext,
                                    "Ошибка 404. Страница по запросу не найдена.",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                            503 -> {
                                Toast.makeText(
                                    applicationContext,
                                    "Ошибка 503. Сбой обращения к серверу.",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                            else -> {
                                noFoundLinear.visibility = View.GONE
                                recyclerViewTracks.visibility = View.GONE
                            }
                        }

                    }

                    override fun onFailure(call: Call<ListTracksResponse>, t: Throwable) {
                        progressBar.visibility = View.GONE
                        noFoundLinear.visibility = View.GONE
                        recyclerViewTracks.visibility = View.GONE
                        historySearchLinear.visibility = View.GONE
                        noConnectLinear.visibility = View.VISIBLE

                    }
                }

                )

        }
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(icSearchFieldRemoveText.windowToken, 0)
    }

    private fun getTextWatcherForSearch(): TextWatcher {
        return object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                icSearchFieldRemoveText.visibility = doVisibilityIconDellText(s)
                textSearchField = searchField.text.toString()

                if (searchField.hasFocus() && s?.isEmpty() == true) {
                    listSearchHistory.clear()
                    listSearchHistory.addAll(searchHistory.getSavedHistorySearch())
                    historySearchAdapter.notifyDataSetChanged()

                 //Checking an empty array. If there are no saved tracks, then we don't show the layout
                    if (listSearchHistory.isNotEmpty()) {
                        historySearchLinear.visibility = View.VISIBLE
                        recyclerViewTracks.visibility = View.GONE
                        noFoundLinear.visibility = View.GONE
                        noConnectLinear.visibility = View.GONE
                    }
                } else {
                    historySearchLinear.visibility = View.GONE
                }


                delaySearchDebounce()


            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }
    }


    private fun getOnFocusTextListener(): View.OnFocusChangeListener {
     return View.OnFocusChangeListener { v, hasFocus ->
         if (hasFocus && searchField.text.isEmpty()) {
             listSearchHistory.clear()
             listSearchHistory.addAll(searchHistory.getSavedHistorySearch())
             historySearchAdapter.notifyDataSetChanged()

             // Checking an empty array. If there are no saved tracks, then we don't show the layout
             if (listSearchHistory.isNotEmpty()) {

                 historySearchLinear.visibility = View.VISIBLE
                 recyclerViewTracks.visibility = View.GONE
                 noFoundLinear.visibility = View.GONE
                 noConnectLinear.visibility = View.GONE
             }

         }
     }
    }

    private fun delaySearchDebounce(){

        handler.removeCallbacks(runnableTask)
        handler.postDelayed(runnableTask, SEARCH_DEBOUNCE_DELAY)
    }

    private fun initializingViews(){

        icSearchFieldRemoveText = findViewById(R.id.ic_search_field_remove_text)
        backButton = findViewById(R.id.back_button)
        searchField = findViewById(R.id.search_field)
        recyclerViewTracks = findViewById(R.id.recyclerView_tracks)
        noFoundLinear = findViewById(R.id.no_found_linearLayout)
        updateButton = findViewById(R.id.update_connect)
        recyclerHistorySearch = findViewById(R.id.recycler_history_search)
        historySearchLinear = findViewById(R.id.history_search_LinearLayout)
        noConnectLinear = findViewById(R.id.no_connect_linearLayout)
        progressBar = findViewById(R.id.progress_bar)
        delButtHistorySearch = findViewById(R.id.del_history_search)
    }

    private fun setListenersForSearch(){

        icSearchFieldRemoveText.setOnClickListener {
            searchField.setText("")

            listTracks.clear()
            tracksAdapter.notifyDataSetChanged()
            recyclerViewTracks.visibility = View.VISIBLE

            hideSoftKeyboard()
        }

        searchField.addTextChangedListener(getTextWatcherForSearch())

        searchField.onFocusChangeListener = getOnFocusTextListener()

        delButtHistorySearch.setOnClickListener {
            getSharedPreferences(App.SAVE_SETTINGS, MODE_PRIVATE)
                .edit()
                .putString(App.KEY_SAVED_SEARCH, "")
                .apply()

            listSearchHistory.clear()
            historySearchLinear.visibility = View.GONE

        }

        updateButton.setOnClickListener {
            doRequestGetOnItunes()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

}
package com.practicum.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.player.ui.model.TrackPlr
import com.practicum.playlistmaker.search.domain.models.Track
import com.practicum.playlistmaker.search.ui.adapter.TracksAdapter


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val KEY_SEARCH = "KEY_SEARCH"
    }

    private lateinit var viewModel: TrackSearchViewModel

    private val listTracks = ArrayList<Track>()
    private val listSearchHistory = ArrayList<Track>()

    private lateinit var tracksAdapter: TracksAdapter
    private lateinit var historySearchAdapter: TracksAdapter

    private var textSearchField: String = ""

    private lateinit var binding: ActivitySearchBinding

    private val handler = Handler(Looper.getMainLooper())


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            TrackSearchViewModel.getViewModelFactory()
        )[TrackSearchViewModel::class.java]

        viewModel.getLiveDataSearchState().observe(this) {
            render(it)
        }

        tracksAdapter = TracksAdapter(listTracks, handler, setListenerForAdapter())
        historySearchAdapter = TracksAdapter(listSearchHistory, handler, setListenerForAdapter())

        binding.searchField.setText(textSearchField)
        binding.recyclerViewTracks.adapter = tracksAdapter
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(this)
        binding.recyclerHistorySearch.adapter = historySearchAdapter
        binding.recyclerHistorySearch.layoutManager = LinearLayoutManager(this)

        setListenersForSearch()
    }

    private fun doVisibilityIconDellText(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

//Save text search by rotate display.

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(KEY_SEARCH, textSearchField)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        textSearchField = savedInstanceState.getCharSequence(KEY_SEARCH).toString()

    }

    private fun hideSoftKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.icSearchFieldRemoveText.windowToken, 0)
    }

    private fun getTextWatcherForSearch(): TextWatcher {
        return object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                binding.icSearchFieldRemoveText.visibility = doVisibilityIconDellText(s)
                textSearchField = binding.searchField.text.toString()

                if (binding.searchField.hasFocus() && s?.isEmpty() == true) {

                    viewModel.setStateSavedSearch()
                } else {
                    viewModel.setStateEmpty()
                }

                viewModel.searchDebounce(s.toString(), false)

            }

            override fun afterTextChanged(s: Editable?) {
                //empty
            }
        }
    }


    private fun getOnFocusTextListener(): View.OnFocusChangeListener {
        return View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus && binding.searchField.text.isEmpty()) {

                viewModel.setStateSavedSearch()

            }
        }
    }

    private fun setListenersForSearch() {

        binding.icSearchFieldRemoveText.setOnClickListener {
            binding.searchField.setText("")

            viewModel.setStateEmpty()

            hideSoftKeyboard()
        }

        binding.searchField.addTextChangedListener(getTextWatcherForSearch())
        binding.searchField.onFocusChangeListener = getOnFocusTextListener()
        binding.delHistorySearch.setOnClickListener {

            viewModel.clearHistorySearch()

        }

        binding.updateConnect.setOnClickListener {
            viewModel.searchDebounce("", true)
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun setListenerForAdapter(): TracksAdapter.TrackClickListener {
        return object : TracksAdapter.TrackClickListener {

            override fun onClickView(track: Track) {

                viewModel.addTrackInHistory(track)

                val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
                intent.putExtra("dataTrack", TrackPlr.mappingTrack(track))
                this@SearchActivity.startActivity(intent)
           }
        }
    }

    private fun render(trackSearchState: TrackSearchState) {
        when (trackSearchState) {
            is TrackSearchState.Empty -> {
                showEmpty()
            }
            is TrackSearchState.ContentSavedSearch -> {
                showSavedSearch(trackSearchState.trackList)
            }
            is TrackSearchState.Content -> {
                showContent(trackSearchState.trackList)
            }
            is TrackSearchState.Error -> {
                showError()
            }
            is TrackSearchState.Loading -> {
                showLoading()
            }
            is TrackSearchState.NotFound -> {
                showNotFound()
            }

        }
    }

    private fun showEmpty() {
        binding.historySearchLinearLayout.visibility = View.GONE
        binding.recyclerHistorySearch.visibility = View.GONE
        binding.recyclerViewTracks.visibility = View.GONE
        binding.noFoundLinearLayout.visibility = View.GONE
        binding.noConnectLinearLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

    }

    private fun showSavedSearch(trackList: List<Track>) {
        listSearchHistory.clear()
        listSearchHistory.addAll(trackList)
        historySearchAdapter.notifyDataSetChanged()

        binding.historySearchLinearLayout.visibility = View.VISIBLE
        binding.recyclerHistorySearch.visibility = View.VISIBLE
        binding.recyclerViewTracks.visibility = View.GONE
        binding.noFoundLinearLayout.visibility = View.GONE
        binding.noConnectLinearLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun showContent(trackList: List<Track>) {
        listTracks.clear()
        listTracks.addAll(trackList)
        tracksAdapter.notifyDataSetChanged()

        binding.historySearchLinearLayout.visibility = View.GONE
        binding.recyclerHistorySearch.visibility = View.GONE
        binding.recyclerViewTracks.visibility = View.VISIBLE
        binding.noFoundLinearLayout.visibility = View.GONE
        binding.noConnectLinearLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

    }

    private fun showError() {
        binding.historySearchLinearLayout.visibility = View.GONE
        binding.recyclerHistorySearch.visibility = View.GONE
        binding.recyclerViewTracks.visibility = View.GONE
        binding.noFoundLinearLayout.visibility = View.GONE
        binding.noConnectLinearLayout.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.updateConnect.visibility = View.VISIBLE

    }

    private fun showLoading() {
        binding.historySearchLinearLayout.visibility = View.GONE
        binding.recyclerHistorySearch.visibility = View.GONE
        binding.recyclerViewTracks.visibility = View.GONE
        binding.noFoundLinearLayout.visibility = View.GONE
        binding.noConnectLinearLayout.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

    }

    private fun showNotFound() {
        binding.historySearchLinearLayout.visibility = View.GONE
        binding.recyclerHistorySearch.visibility = View.GONE
        binding.recyclerViewTracks.visibility = View.GONE
        binding.noFoundLinearLayout.visibility = View.VISIBLE
        binding.noConnectLinearLayout.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

    }
}
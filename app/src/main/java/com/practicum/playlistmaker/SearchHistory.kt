package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.Track

class SearchHistory(private val savedHistorySearch: SharedPreferences) {

    private var tempListTracks: ArrayList<Track> = arrayListOf()


    fun getSavedHistorySearch(): ArrayList<Track> {

        val jsonString = savedHistorySearch.getString(App.KEY_SAVED_SEARCH, null)

        if (jsonString == null || jsonString.equals("")) {

            return ArrayList<Track>()
        }

        //Get class type Type! for correct work Gson deserialization
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type

        return Gson().fromJson(jsonString, itemType)

    }


    fun addTrackInHistory(trackData: Track) {

        var isIdMatches: Boolean = false
        var indexItem: Int = -1

        tempListTracks = getSavedHistorySearch()

        //Search for matches in the list
        tempListTracks.forEachIndexed { index, track ->
            if (track.trackId == trackData.trackId) {
                isIdMatches = true
                indexItem = index
            }
        }
        //Adding to the list depending on the match
        if (isIdMatches) {
            tempListTracks.removeAt(indexItem)
            tempListTracks.add(0, trackData)
        } else {

            tempListTracks.add(0, trackData)
        }

        //The logic of selecting the first ten tracks
        if (tempListTracks.size <= 10) {
            savedHistorySearch.edit()
                .putString(App.KEY_SAVED_SEARCH, Gson().toJson(tempListTracks))
                .apply()
        } else {
            val shortListTracks = tempListTracks.dropLast(tempListTracks.size - 10)

            savedHistorySearch.edit()
                .putString(App.KEY_SAVED_SEARCH, Gson().toJson(shortListTracks))
                .apply()
        }

    }

}
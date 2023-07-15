package com.practicum.playlistmaker.search.data.local.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.local.LocalStorage

class LocalStorageImpl (private val sharedPref: SharedPreferences,
                        private val gson: Gson) : LocalStorage {

    companion object {
        const val KEY_SAVED_SEARCH = "key_saved_search"
    }

    override fun getSavedHistorySearch(): ArrayList<TrackDto> {

        val jsonString = sharedPref.getString(KEY_SAVED_SEARCH, null)

        if (jsonString == null || jsonString.equals("")) {

            return ArrayList<TrackDto>()
        }

        //Get class type Type! for correct work Gson deserialization
        val itemType = object : TypeToken<ArrayList<TrackDto>>() {}.type

        return gson.fromJson(jsonString, itemType)
    }

    override fun addTrackListInHistory(sortedListTrack:ArrayList<TrackDto>) {

        sharedPref.edit()
            .putString(KEY_SAVED_SEARCH, gson.toJson(sortedListTrack))
            .apply()
    }

    override fun clearHistorySearch() {

       sharedPref.edit()
            .putString(KEY_SAVED_SEARCH, "")
            .apply()
    }
}
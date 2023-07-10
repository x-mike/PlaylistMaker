package com.practicum.playlistmaker.search.data.local.impl

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.local.LocalStorage

class LocalStorageImpl (context: Context) : LocalStorage {

    companion object {
        const val KEY_SAVED_SEARCH = "key_saved_search"
    }

    val savedHistorySearch = context.getSharedPreferences(KEY_SAVED_SEARCH,Context.MODE_PRIVATE)


    override fun getSavedHistorySearch(): ArrayList<TrackDto> {

        val jsonString = savedHistorySearch.getString(KEY_SAVED_SEARCH, null)

        if (jsonString == null || jsonString.equals("")) {

            return ArrayList<TrackDto>()
        }

        //Get class type Type! for correct work Gson deserialization
        val itemType = object : TypeToken<ArrayList<TrackDto>>() {}.type

        return Gson().fromJson(jsonString, itemType)
    }

    override fun addTrackListInHistory(sortedListTrack:ArrayList<TrackDto>) {

        savedHistorySearch.edit()
            .putString(KEY_SAVED_SEARCH, Gson().toJson(sortedListTrack))
            .apply()

    }

    override fun clearHistorySearch() {

        savedHistorySearch
            .edit()
            .putString(KEY_SAVED_SEARCH, "")
            .apply()

    }
}
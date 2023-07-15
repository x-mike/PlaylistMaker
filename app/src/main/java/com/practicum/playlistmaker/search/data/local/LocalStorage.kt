package com.practicum.playlistmaker.search.data.local

import com.practicum.playlistmaker.search.data.dto.TrackDto

interface LocalStorage {

   fun getSavedHistorySearch(): ArrayList<TrackDto>
   fun addTrackListInHistory(sortedListTrack:ArrayList<TrackDto>)
   fun clearHistorySearch()

}
package com.practicum.playlistmaker.search.data.storage

import com.practicum.playlistmaker.search.data.dto.TrackDto

interface HistoryStorage {

   fun getSavedHistorySearch(): ArrayList<TrackDto>
   fun addTrackListInHistory(sortedListTrack:ArrayList<TrackDto>)
   fun clearHistorySearch()

}
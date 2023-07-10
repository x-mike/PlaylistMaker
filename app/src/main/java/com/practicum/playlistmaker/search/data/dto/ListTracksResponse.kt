package com.practicum.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

class ListTracksResponse(@SerializedName("results") val listTracks: ArrayList<TrackDto>) :
    Response()
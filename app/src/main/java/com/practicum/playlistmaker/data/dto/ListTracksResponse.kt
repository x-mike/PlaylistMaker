package com.practicum.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName
import com.practicum.playlistmaker.domain.models.Track

class ListTracksResponse (@SerializedName ("results") val listTracks: ArrayList<Track>)
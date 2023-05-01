package com.practicum.playlistmaker.data

import com.google.gson.annotations.SerializedName

class ListTracksResponse (@SerializedName ("results") val listTracks: ArrayList<Track>)
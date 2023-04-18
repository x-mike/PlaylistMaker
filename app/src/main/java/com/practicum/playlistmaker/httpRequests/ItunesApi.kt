package com.practicum.playlistmaker.httpRequests

import com.practicum.playlistmaker.data.ListTracksResponse
import retrofit2.Call

interface ItunesApi {

    @retrofit2.http.GET("/search?entity=song")
    fun search(@retrofit2.http.Query("term") text: String): Call<ListTracksResponse>

}
package com.practicum.playlistmaker.search.data.network.impl

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.network.ItunesApi
import com.practicum.playlistmaker.search.data.network.NetworkClient
import java.io.IOException

class NetworkClientImpl(private val itunesService:ItunesApi): NetworkClient {

    override fun doRequest(dtoRequest: Any): Response {
        return try {
            if (dtoRequest is TrackSearchRequest) {
                val response = itunesService.search(dtoRequest.requestSearch).execute()

                val body = response.body() ?: Response()

                body.apply { responseCode = response.code() }
            } else {
                Response().apply { responseCode = 400 }
            }

        }catch (exp: IOException){
                Response()
        }
    }
}
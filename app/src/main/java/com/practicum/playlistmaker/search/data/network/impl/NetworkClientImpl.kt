package com.practicum.playlistmaker.search.data.network.impl

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.network.ItunesApi
import com.practicum.playlistmaker.search.data.network.NetworkClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkClientImpl(private val itunesService: ItunesApi) : NetworkClient {

    override suspend fun doRequest(dtoRequest: Any): Response {
        return withContext(Dispatchers.IO) {
            try {
                if (dtoRequest is TrackSearchRequest) {

                    val response = itunesService.search(dtoRequest.requestSearch)
                    response.apply { responseCode = 200 }

                } else {
                    Response().apply { responseCode = 400 }
                }
            } catch (exp: Throwable) {
                Response()
            }
        }
    }
}
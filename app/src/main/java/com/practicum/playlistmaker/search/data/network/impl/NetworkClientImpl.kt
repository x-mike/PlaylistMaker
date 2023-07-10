package com.practicum.playlistmaker.search.data.network.impl

import com.practicum.playlistmaker.search.data.dto.Response
import com.practicum.playlistmaker.search.data.dto.TrackSearchRequest
import com.practicum.playlistmaker.search.data.network.ItunesApi
import com.practicum.playlistmaker.search.data.network.NetworkClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class NetworkClientImpl(): NetworkClient {


    //Interceptor for take logs about request http
    private val interceptorHttp = HttpLoggingInterceptor().apply {
        this.level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptorHttp)
        .build()

    private val itunesBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val itunesService = retrofit.create(ItunesApi::class.java)

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